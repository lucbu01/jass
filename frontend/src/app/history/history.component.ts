import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
})
export class HistoryComponent implements OnInit {
  notFound = false;
  history: any;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((data) => {
      if (data.id) {
        this.http.get<any>(`/api/history/${data.id}`).subscribe(
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
