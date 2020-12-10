import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { WebSocketService } from '../services/web-socket.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, OnDestroy {
  name = '';
  subscriptions: Subscription[] = [];

  constructor(private http: HttpClient, private webSocketService: WebSocketService, private router: Router) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.webSocketService.tryConnect().subscribe(result => console.log(`WebSocket connected: ${result}`)),
      this.webSocketService.event<string>('/user/private/name').subscribe(name => this.name = name),
      this.webSocketService.event<string>('/user/private/game/created').subscribe(game => this.router.navigate(['/lobby', game]))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  create(): void {
    this.setUsername(() => this.webSocketService.send('/public/game/create'));
  }

  join(): void {
    this.setUsername(() => {});
  }

  setUsername(callback: () => void): void {
    if (this.webSocketService.connected) {
      this.webSocketService.sendString('/private/name', this.name);
      callback();
    } else {
      this.http.post<string>('/api/name', JSON.stringify(this.name)).subscribe(uuid => {
        this.webSocketService.tryConnect(uuid).subscribe(result => {
          console.log(`WebSocket connected: ${result}`);
          if (result === true) {
            setTimeout(() => callback());
          }
        });
      });
    }
  }
}
