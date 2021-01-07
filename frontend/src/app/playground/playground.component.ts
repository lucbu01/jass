import { Component, OnInit } from '@angular/core';
import { Card, Game } from 'src/model/model';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../services/web-socket.service';

@Component({
  selector: 'app-playground',
  templateUrl: './playground.component.html',
  styleUrls: ['./playground.component.scss'],
})
export class PlaygroundComponent implements OnInit {
  playerName1 = 'Spieler 1';
  playerName2 = 'Spieler 2';
  playerName3 = 'Spieler 3';
  playerName4 = 'Spieler 4';
  scoreTeam1 = 0;
  scoreTeam2 = 0;
  playedCard: string | undefined = undefined;
  playedCardMate: string | undefined = undefined;
  playedCardOpponent1: string | undefined = undefined;
  playedCardOpponent2: string | undefined = undefined;
  subscriptions: Subscription[] = [];
  gameSubscription?: Subscription;
  handSubscription?: Subscription;
  hand: Card[] = [];
  game?: Game;
  hasToAnnounce = false;
  playerIds: string[] = [];
  playerNames: string[] = [];
  myName = 'Ich';
  namePartner = 'Partner';
  nameOpponent1 = 'Gegner 1';
  nameOpponent2 = 'Gegner 2';
  hint = 'Jass Spiel laden . . .';
  myIndex = 0;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.subscribe((data) => {
        if (this.gameSubscription) {
          this.gameSubscription.unsubscribe();
        }
        if (data.id) {
          if (this.handSubscription) {
            this.handSubscription.unsubscribe();
            this.handSubscription = undefined;
          }
          if (this.gameSubscription) {
            this.gameSubscription.unsubscribe();
          }
          this.gameSubscription = this.webSocketService
            .data<Game>(`/public/game/${data.id}`)
            .subscribe((game) => {
              if (game.scoreboard && game.scoreboard.finished) {
                this.router.navigate(['history', game.id]);
              }
              if (!this.handSubscription) {
                this.handSubscription = this.webSocketService
                  .data<Card[]>(`/user/private/game/${game.id}/hand`)
                  .subscribe((hand) => {
                    this.hand = hand;
                  });
              }
              this.game = this.assignDeep(this.game ? this.game : {}, game);
              this.playerName1 = this.game.teams[0].players[0].name;
              this.playerName2 = this.game.teams[0].players[1].name;
              this.playerName3 = this.game.teams[1].players[0].name;
              this.playerName4 = this.game.teams[1].players[1].name;
              this.playerIds = [
                this.game.teams[0].players[0].id,
                this.game.teams[1].players[0].id,
                this.game.teams[0].players[1].id,
                this.game.teams[1].players[1].id,
              ];
              if (this.webSocketService.userId) {
                this.myIndex = this.playerIds.indexOf(
                  this.webSocketService.userId?.split(':')[0]
                );
              }
              this.playerNames = [
                this.game.teams[0].players[0].name,
                this.game.teams[1].players[0].name,
                this.game.teams[0].players[1].name,
                this.game.teams[1].players[1].name,
              ];
              this.myName = this.getPlayerName(0);
              this.nameOpponent2 = this.getPlayerName(1);
              this.namePartner = this.getPlayerName(2);
              this.nameOpponent1 = this.getPlayerName(3);
              this.hint = '';
              if (game.match && !game.match.type) {
                if (
                  this.playerIds[this.myIndex] ===
                  game.match.definitiveAnnouncer.id
                ) {
                  this.hasToAnnounce = true;
                  this.hint = 'Du darfst ansagen';
                } else {
                  this.hasToAnnounce = false;
                  this.hint = `${game.match.definitiveAnnouncer.name} darf ansagen`;
                }
              }
              if (game.round) {
                if (game.round.turnOf) {
                  if (
                    this.webSocketService.userId?.split(':')[0] ===
                    game.round.turnOf.id
                  ) {
                    this.hint = 'Du bist an der Reihe';
                  } else {
                    this.hint = `${game.round.turnOf.name} ist an der Reihe`;
                  }
                }
                this.playedCard = this.getCard(0);
                this.playedCardOpponent2 = this.getCard(1);
                this.playedCardMate = this.getCard(2);
                this.playedCardOpponent1 = this.getCard(3);
              }
              if (game.scoreboard) {
                this.scoreTeam1 = game.scoreboard.teamOnePoints;
                this.scoreTeam2 = game.scoreboard.teamTwoPoints;
              }
            });
        }
      })
    );
  }

  assignDeep(target: any, source: any): Game {
    Object.keys(source).forEach((key) => {
      const targetValue = target[key];
      const sourceValue = source[key];
      if (
        targetValue &&
        typeof targetValue === 'object' &&
        typeof sourceValue === 'object' &&
        !Array.isArray(targetValue)
      ) {
        target[key] = this.assignDeep(targetValue, sourceValue);
      } else {
        target[key] = sourceValue;
      }
    });
    return target;
  }

  cardClick(card: Card): void {
    if (this.game) {
      this.webSocketService.send(`/public/game/${this.game.id}/play`, card);
    }
  }

  push(): void {
    if (this.game) {
      this.webSocketService.send(`/public/game/${this.game.id}/push`);
      this.hasToAnnounce = false;
    }
  }

  announce(type: string): void {
    if (this.game) {
      this.webSocketService.send(`/public/game/${this.game.id}/announce`, type);
      this.hasToAnnounce = false;
    }
  }

  indexOf(myIndexPlus: number): number {
    let index = this.myIndex + myIndexPlus;
    if (index > 3) {
      index -= 4;
    }
    return index;
  }

  getCard(myIndexPlus: number): string {
    if (this.game && this.game.round) {
      const moves: any[] = this.game.round.moves;
      const move = moves.find(
        (mv) => mv.player.id === this.playerIds[this.indexOf(myIndexPlus)]
      );
      if (move) {
        return `${move.card.color}_${move.card.value}`;
      } else {
        return '';
      }
    }
    return '';
  }

  getPlayerName(myIndexPlus: number): string {
    return this.playerNames[this.indexOf(myIndexPlus)];
  }
}
