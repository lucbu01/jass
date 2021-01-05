import { Component, OnInit } from '@angular/core';
import { Card } from 'src/model/card';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../services/web-socket.service';

@Component({
  selector: 'app-playground',
  templateUrl: './playground.component.html',
  styleUrls: ['./playground.component.scss']
})
export class PlaygroundComponent implements OnInit {
  playerName1 = 'player1';
  playerName2 = 'player2';
  playerName3 = 'player3';
  playerName4 = 'player4';
  scoreTeam1 = 0;
  scoreTeam2 = 0;
  playedCard: string | undefined = undefined;
  playedCardMate: string | undefined = undefined;
  playedCardOpponent1: string | undefined = undefined;
  playedCardOpponent2: string | undefined = undefined;
  cards: Card[] = [];
  subscriptions: Subscription[] = [];
  gameSubscription?: Subscription;
  handSubscription?: Subscription;
  hand: any = [];
  game: any = {};
  hasToAnnounce = false;
  playerIds: string[] = [];
  myIndex = 0;

  constructor(private router: Router, private route: ActivatedRoute,private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.subscribe(data => {
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
          this.gameSubscription = this.webSocketService.data<any>(`/public/game/${data.id}`).subscribe(game => {
            if (!this.handSubscription) {
              this.handSubscription = this.webSocketService.data<any>(`/user/private/game/${game.id}/hand`).subscribe(hand => {
                this.hand = hand;
              });
            }
            this.game = this.assignDeep(this.game, game);
            this.playerName1 = this.game.teams[0].players[0].name;
            this.playerName2 = this.game.teams[0].players[1].name;
            this.playerName3 = this.game.teams[1].players[0].name;
            this.playerName4 = this.game.teams[1].players[1].name;
            this.playerIds = [
              this.game.teams[0].players[0].id,
              this.game.teams[1].players[0].id,
              this.game.teams[0].players[1].id,
              this.game.teams[1].players[1].id
            ];
            if (this.webSocketService.userId) {
              this.myIndex = this.playerIds.indexOf(this.webSocketService.userId?.split(':')[0]);
            }
            if (game.match) {
              if (!game.match.type && this.playerIds[this.myIndex] === game.match.definitiveAnnouncer.id) {
                this.hasToAnnounce = true;
              };
            }
            if (game.round) {
              this.playedCard = this.getCard(0);
              this.playedCardOpponent2 = this.getCard(1);
              this.playedCardMate = this.getCard(2);
              this.playedCardOpponent1 = this.getCard(3);
            }
            if (game.scoreboard) {
              this.scoreTeam1 = this.game.scoreboard.teamOnePoints;
              this.scoreTeam2 = this.game.scoreboard.teamTwoPoints;
            }
          });
        }
      })
    );
  }

  assignDeep(target: any, source: any) {
    Object.keys(source).forEach(key => {
      const targetValue = target[key];
      const sourceValue = source[key];
      if (targetValue && typeof(targetValue) === 'object' && typeof(sourceValue) === 'object' && !Array.isArray(targetValue)) {
        target[key] = this.assignDeep(targetValue, sourceValue);
      } else {
        target[key] = sourceValue;
      }
    });
    return target;
  }

  cardClick(card: Card): void {
    this.webSocketService.send(`/public/game/${this.game.id}/play`, card);
  }

  push(): void {
    this.webSocketService.send(`/public/game/${this.game.id}/push`);
    this.hasToAnnounce = false;
  }

  announce(type: string): void {
    this.webSocketService.send(`/public/game/${this.game.id}/announce`, type);
    this.hasToAnnounce = false;
  }

  indexOf(myIndexPlus: number): number {
    let index = this.myIndex + myIndexPlus;
    if (index > 3) {
      index -= 4;
    }
    return index;
  }

  getCard(myIndexPlus: number): string {
    const moves: any[] = this.game.round.moves;
    const move = moves.find(mv => mv.player.id === this.playerIds[this.indexOf(myIndexPlus)]);
    if (move) {
      return `${move.card.color}_${move.card.value}`;
    } else {
      return '';
    }
  }

}
