import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { WebSocketService } from '../services/web-socket.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { JoinDialogComponent } from './join-dialog/join-dialog.component';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit, OnDestroy {
  name = '';
  subscriptions: Subscription[] = [];
  redirectTo: string | undefined;

  constructor(
    private http: HttpClient,
    private webSocketService: WebSocketService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.queryParams.subscribe(
        (params) => (this.redirectTo = params.redirectTo)
      ),
      this.webSocketService
        .tryConnect()
        .subscribe((result) => console.log(`WebSocket connected: ${result}`)),
      this.webSocketService
        .event<string>('/user/private/name')
        .subscribe((name) => (this.name = name)),
      this.webSocketService
        .event<string>('/user/private/game/created')
        .subscribe((game) => this.router.navigate(['/lobby', game]))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((s) => s.unsubscribe());
  }

  create(): void {
    this.setUsername(() => this.webSocketService.send('/public/game/create'));
  }

  join(): void {
    this.setUsername(() => {
      if (this.redirectTo) {
        this.router.navigateByUrl(this.redirectTo);
      } else {
        this.dialog
          .open(JoinDialogComponent)
          .afterClosed()
          .subscribe((data) => {
            if (data) {
              this.router.navigate(['/lobby', data]);
            }
          });
      }
    });
  }

  setUsername(callback: () => void): void {
    if (this.webSocketService.connected) {
      this.webSocketService.sendString('/private/name', this.name);
      callback();
    } else {
      this.http
        .post<string>('/api/name', JSON.stringify(this.name))
        .subscribe((uuid) => {
          this.webSocketService.tryConnect(uuid).subscribe((result) => {
            console.log(`WebSocket connected: ${result}`);
            if (result === true) {
              setTimeout(() => callback());
            }
          });
        });
    }
  }
}
