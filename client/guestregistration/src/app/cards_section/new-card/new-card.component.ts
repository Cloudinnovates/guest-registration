import { Component, OnInit } from '@angular/core';
import { Card } from '../../classes/card';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { CardService } from '../../services/card.service';
import { Office } from '../../classes/offices';
import { OfficeService } from '../../services/office.service';

@Component({
  selector: 'app-new-card',
  templateUrl: './new-card.component.html',
  styleUrls: ['./new-card.component.css']
})
export class NewCardComponent implements OnInit {

  card: Card = {
    id: "",
    cardNo: undefined,
    location: "",
    cardType: "",
    validThru: 0,
    validFrom: 0,
    cardAvailable: true
  };
  date = new Date();
  types: string[] = []
  offices: Office[];
  validThru = ""
  errorMessage = "";

  constructor(private _router: Router, private _cardService: CardService, private _officeService: OfficeService) { }

  ngOnInit() {
    this.getTypes()
    this.getLocations()
  }

  getTypes() {
    this._cardService.getTypes().subscribe(
      data => {

        this.types = [];
        Object.keys(data).forEach(key => {
          this.types.push(data[key]);
        });
        this.card.cardType = this.types[0];
      },
      err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }
      }
    )
  }

  getLocations(){
    this._officeService.getOffices().subscribe(offices => {
      this.offices = [];
      Object.keys(offices).forEach(key => {
        this.offices.push(offices[key]);
      })
      // console.log(this.offices);
    },
    err => {
      if (err['status'] == 401){
        this._router.navigate(['/auth-error']);
      }
    }
    );
  }

  /**
   * sends post request to the server with user provided data 
   * to create new card entery in the database
   */
  onSubmit() {
    console.log(this.card);
    let day = "" + this.date.getDate();
    let month = "" + (this.date.getMonth() + 1);
    let year = "" + this.date.getFullYear();

    if (day.length == 1) {
      day = "0" + day
    }

    if (month.length == 1) {
      month = "0" + month
    }
    this.card.validFrom = new Date().getTime();
    let date = new Date(new DatePipe('en-US').transform(this.validThru, 'MM-dd-yyyy') + " 12:00:00")
    this.card.validThru = date.getTime();
    if (this.card.cardNo != undefined && this.card.cardType != "" && this.card.location != "" && this.validThru != "") {
      console.log(this.card);
      this._cardService.postCard(this.card).subscribe(
        data => {
          this._router.navigate(['/cards']);
        },
        err => {
          let element = document.getElementById('error-alert');
          element.style.display = "block";
          if (err['error']['error']) {
            this.errorMessage = err['error']['error'] + ", try again later";
          } else {
            this.errorMessage = err['error'] + ", try again later";
          }
        }

      )

    }
  }
}
