import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { WebSocketService } from 'src/app/services/web-socket.service';
import { PublicGame } from 'src/model/model';

@Component({
  selector: 'app-join-dialog',
  templateUrl: './join-dialog.component.html',
  styleUrls: ['./join-dialog.component.scss'],
})
export class JoinDialogComponent implements OnInit {
  form = new FormGroup({
    gameId: new FormControl('', Validators.required),
  });

  games: PublicGame[] = [];

  constructor(
    public dialogRef: MatDialogRef<JoinDialogComponent>,
    public webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.webSocketService
      .data<PublicGame[]>('/public/games')
      .subscribe(
        (games) => (this.games = games.filter((game) => game.players < 4))
      );
  }

  join(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value.gameId);
    }
  }

  joinPublic(game: any): void {
    this.dialogRef.close(game.id);
  }
}
