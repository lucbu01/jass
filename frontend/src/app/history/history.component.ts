import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GameHistory } from 'src/model/model';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {
  notFound = false;
  history?: GameHistory;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((data) => {
      if (data.id) {
        this.http.get<GameHistory>(`/api/history/${data.id}`).subscribe(
          (history) => {
            this.history = history;
            this.notFound = false;
          },
          () => {
            this.history = undefined;
            this.notFound = true;
          }
        );
      }
    });
  }
}
