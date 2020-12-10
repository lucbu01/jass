import { Injectable, OnDestroy } from '@angular/core';
import { EMPTY, Observable, of } from 'rxjs';
import { first, map, share, switchMap } from 'rxjs/operators';
import { RxStomp } from '@stomp/rx-stomp';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService implements OnDestroy {

  private client = new RxStomp();
  private connectedUser?: string;
  private currentConnectionTry = of(true).pipe(switchMap(() => {
    const userId = this.userId;
    if (this.client.connected() && this.connectedUser === userId) {
      return of(true);
    } else if (userId) {
      return new Observable<boolean>(observer => {
        this.client.configure({
          brokerURL: `ws${location.protocol === 'https:' ? 's' : ''}://${location.host}/ws`,
          connectHeaders: { user: this.userId as string },
          reconnectDelay: 0
        });
        this.client.connected$.pipe(first()).subscribe(() => {
          this.connectedUser = userId;
          observer.next(true);
          observer.complete();
          if (environment.production) {
            window.addEventListener('online', this.onlineEventListener);
            window.addEventListener('focus', this.onlineEventListener);
          }
        });
        this.client.stompErrors$.pipe(first()).subscribe(() => {
          observer.next(false);
          observer.complete();
        });
        this.client.deactivate().then(() => this.client.activate());
      });
    } else {
      return of(false);
    }
  })).pipe(share());

  constructor(private router: Router, private snack: MatSnackBar) {
    this.event<{ message: string; redirectTo?: string }>('/user/private/messages').subscribe(message => {
      this.snack.open(message.message, undefined, { duration: 5000 });
      if (message.redirectTo) {
        this.router.navigateByUrl(message.redirectTo);
      }
    });
  }

  get userId(): string | undefined  {
    const userId = localStorage.getItem('userId');
    return userId ? userId : undefined;
  }

  get connected(): boolean {
    return this.client.connected();
  }

  tryConnect(userId?: string): Observable<boolean> {
    if (userId) {
      localStorage.setItem('userId', userId);
    }
    return this.currentConnectionTry;
  }

  logout(): void {
    this.router.navigateByUrl('/');
    this.disconnect();
    localStorage.clear();
    window.removeEventListener('online', this.onlineEventListener);
    window.removeEventListener('focus', this.onlineEventListener);
  }

  send<T>(destination: string, body?: T, headers?: { [key: string]: any }): void {
    this.sendString(destination, JSON.stringify(body), headers);
  }

  sendString(destination: string, body: string, headers?: { [key: string]: any }): void {
    this.tryConnect().subscribe(connected => {
      if (connected) {
        this.client.publish({
          destination, body, headers
        });
      } else {
        this.navigateToLoginPage();
      }
    });
  }

  event<T>(destination: string): Observable<T> {
    return this.client.watch(destination).pipe(map(value => JSON.parse(value.body))).pipe(share());
  }

  data<T>(destination: string): Observable<T> {
    return this.tryConnect().pipe(switchMap(connected => {
      if (connected) {
        return this.client.watch(destination).pipe(map(value => JSON.parse(value.body)));
      } else {
        this.navigateToLoginPage();
        return EMPTY;
      }
    }));
  }

  ngOnDestroy(): void {
    this.disconnect();
  }

  private navigateToLoginPage(): void {
    window.removeEventListener('online', this.onlineEventListener);
    window.removeEventListener('focus', this.onlineEventListener);
    if (!this.router.isActive('/', true)) {
      this.router.navigate(['/']);
    }
  }

  private onlineEventListener = () => {
    this.tryConnect().subscribe(connected => {
      if (!connected) {
        this.navigateToLoginPage();
      }
    });
  };

  private disconnect(): void {
    if (this.client.connected()) {
      this.client.deactivate();
    }
  }
}
