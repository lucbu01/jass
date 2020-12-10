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
  scoreTeam1 = '0';
  scoreTeam2 = '0';
  playedCard = '';
  playedCardMate = '';
  playedCardOpponent1 = '';
  playedCardOpponent2 = '';
  cards: Card[] = [];
  subscriptions: Subscription[] = [];
  gameSubscription?: Subscription;
  game: any;

  constructor(private router: Router, private route: ActivatedRoute,private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.subscribe(data => {
        if (this.gameSubscription) {
          this.gameSubscription.unsubscribe();
        }
        if (data.id) {
          this.gameSubscription = this.webSocketService.data(`/public/game/${data.id}`).subscribe(game => {
            this.game = game;
            this.playerName1 = this.game.teams[0].players[0].name;
            this.playerName2 = this.game.teams[0].players[1].name;
            this.playerName3 = this.game.teams[1].players[0].name;
            this.playerName4 = this.game.teams[1].players[1].name;
          });
        }
      })
    );
  }

  cardClicked(card: Card): void {
  }

}
