import { Injectable, OnDestroy } from '@angular/core';
import { EMPTY, Observable, of, ReplaySubject } from 'rxjs';
import { first, map, multicast, refCount, share, switchMap } from 'rxjs/operators';
import { RxStomp } from '@stomp/rx-stomp';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService implements OnDestroy {

  constructor(private router: Router) { }

  private client = new RxStomp();
  private connectedUser?: string;
  private events: { [destination: string]: Observable<any> } = {};
  private datas: { [destination: string]: Observable<any> } = {};
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
          window.addEventListener('online', this.onlineEventListener);
          window.addEventListener('focus', this.onlineEventListener);
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

  get userId(): string | undefined  {
    const userId = localStorage.getItem('userId');
    return userId ? userId : undefined;
  }

  get connected(): boolean {
    return this.client.connected();
  }

  private navigateToLoginPage(): void {
    window.removeEventListener('online', this.onlineEventListener);
    window.removeEventListener('focus', this.onlineEventListener);
    if (!this.router.isActive('/', false)) {
      this.router.navigate(['/'], { queryParams: { redirectTo: this.router.url }});
    }
  }

  private onlineEventListener = () => {
    this.tryConnect().subscribe(connected => {
      if (!connected) {
        this.navigateToLoginPage();
      }
    });
  }

  tryConnect(userId?: string): Observable<boolean> {
    if (userId) {
      localStorage.setItem('userId', userId);
    }
    return this.currentConnectionTry;
  }

  private disconnect(): void {
    if (this.client.connected()) {
      this.client.deactivate();
    }
  }

  logout(): void {
    this.router.navigateByUrl('/login');
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
    if (!this.events[destination]) {
      this.events[destination] =  this.client.watch(destination).pipe(map(value => JSON.parse(value.body))).pipe(share());
    }
    return this.events[destination];
  }

  data<T>(destination: string): Observable<T> {
    if (!this.datas[destination]) {
      this.datas[destination] = this.tryConnect().pipe(switchMap(connected => {
        if (connected) {
          return this.client.watch(destination).pipe(map(value => JSON.parse(value.body)));
        } else {
          this.navigateToLoginPage();
          return EMPTY;
        }
      })).pipe(multicast(new ReplaySubject()), refCount());
    }
    return this.datas[destination];
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}