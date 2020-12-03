import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { WebSocketService } from '../services/web-socket.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  name = '';

  constructor(private http: HttpClient, private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.webSocketService.tryConnect().subscribe(result => console.log(`WebSocket connected: ${result}`));
    this.webSocketService.event<string>('/user/private/name').subscribe(name => this.name = name);
  }

  create(): void {
    this.setUsername();
  }

  join(): void {
    this.setUsername();
  }

  setUsername(): void {
    if (this.webSocketService.connected) {
      this.webSocketService.sendString('/private/name', this.name);
    } else {
      this.http.post<string>('/api/name', JSON.stringify(this.name)).subscribe(uuid => {
        this.webSocketService.tryConnect(uuid).subscribe(result => console.log(`WebSocket connected: ${result}`));
      });
    }
  }
}
