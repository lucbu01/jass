import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-join-dialog',
  templateUrl: './join-dialog.component.html',
  styleUrls: ['./join-dialog.component.scss'],
})
export class JoinDialogComponent implements OnInit {
  form = new FormGroup({
    gameId: new FormControl('', Validators.required),
  });

  constructor(public dialogRef: MatDialogRef<JoinDialogComponent>) {}

  ngOnInit(): void {}

  join(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value.gameId);
    }
  }
}
