import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  name = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  create(): void {
    this.createUuid();
  }

  join(): void {
    this.createUuid();
  }

  createUuid(): void {
    this.http.post('/api/name', JSON.stringify(name)).subscribe(uuid => {
      localStorage.setItem('uuid', JSON.stringify(uuid));
    });
  }
}
