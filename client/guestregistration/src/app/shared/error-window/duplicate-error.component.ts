import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';


@Component({
  selector: 'agent-error-dialog',
  templateUrl: './duplicate-error.component.html',
  styleUrls: ['./duplicate-error.component.css']
})

export class DuplicateErrorComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DuplicateErrorComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any){}

  ngOnInit() {

  }

  public close() {
    this.dialogRef.close();
  }

}
