import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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

  constructor(private router: Router, private route: ActivatedRoute, private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.subscribe(data => {
        if (this.gameSubscription) {
          this.gameSubscription.unsubscribe();
        }
        if (data.id) {
          this.gameSubscription = this.webSocketService.data(`/public/game/${data.id}`).subscribe(game => {
            this.game = game;
            if (this.game.started) {
              this.router.navigate(['/play', this.game.id]);
              console.log(`Game ${this.game.id} started!`);
            }
          });
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
      this.webSocketService.send(`/public/game/${this.game.id}/start`);
    }
  }

  canStart(): boolean {
    return this.game && this.game.teams[0].players.length === 2 && this.game.teams[1].players.length === 2;
  }

  invitationLinkClicked(event: Event): void {
    (event.target as HTMLInputElement).select();
  }

}
