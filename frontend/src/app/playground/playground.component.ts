import { Component, OnInit } from '@angular/core';
import { Card } from 'src/model/card';

@Component({
  selector: 'app-playground',
  templateUrl: './playground.component.html',
  styleUrls: ['./playground.component.scss']
})
export class PlaygroundComponent implements OnInit {
  playerName1 = 'player1';
  playerName2 = 'player2';
  playerName3 = 'player3';
  playerName4 = 'player4';
  countTeam1 = 'count1';
  countTeam2 = 'count2';
  cards: Card[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
