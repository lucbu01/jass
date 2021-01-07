import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Game } from 'src/model/model';
import { WebSocketService } from '../services/web-socket.service';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss'],
})
export class LobbyComponent implements OnInit, OnDestroy {
  game?: Game;
  subscriptions: Subscription[] = [];
  gameSubscription?: Subscription;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    setTimeout(() =>
      this.subscriptions.push(
        this.route.params.subscribe((data) => {
          if (this.gameSubscription) {
            this.gameSubscription.unsubscribe();
          }
          if (data.id) {
            this.gameSubscription = this.webSocketService
              .data<Game>(`/public/game/${data.id}`)
              .subscribe((game) => {
                this.game = game;
                if (this.game.started) {
                  this.router.navigate(['/play', game.id]);
                  console.log(`Game ${game.id} started!`);
                }
              });
          }
        })
      )
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((s) => s.unsubscribe());
    if (this.gameSubscription) {
      this.gameSubscription.unsubscribe();
    }
  }

  start(): void {
    if (this.game && this.canStart()) {
      this.webSocketService.send(`/public/game/${this.game.id}/start`);
    }
  }

  canStart(): boolean {
    if (
      this.game &&
      this.game.teams &&
      this.game.teams[0].players.length === 2 &&
      this.game.teams[1].players.length === 2
    ) {
      return true;
    } else {
      return false;
    }
  }

  invitationLinkClicked(event: Event): void {
    (event.target as HTMLInputElement).select();
  }

  getLink(): string {
    if (this.game) {
      return `${location.protocol}//${location.host}/lobby/${this.game.id}`;
    } else {
      return '';
    }
  }
}
