import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../services/web-socket.service';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit, OnDestroy {

  game: any;
  subscriptions: Subscription[] = [];
  gameSubscription?: Subscription;

  constructor(private route: ActivatedRoute, private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.subscribe(data => {
        if (this.gameSubscription) {
          this.gameSubscription.unsubscribe();
        }
        if (data.id) {
          this.gameSubscription = this.webSocketService.data(`/public/game/${data.id}`).subscribe(game => this.game = game);
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
    if (this.gameSubscription) {
      this.gameSubscription.unsubscribe();
    }
  }

  start(): void {
    if (this.canStart()) {
      // do
    }
  }

  canStart(): boolean {
    return this.game && this.game.teams[0].players.length === 2 && this.game.teams[1].players.length === 2;
  }

}
