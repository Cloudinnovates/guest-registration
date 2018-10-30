import { Component, OnInit } from '@angular/core';
import { Card } from '../../classes/card';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { CardService } from '../../services/card.service';
import { OfficeService } from '../../services/office.service';
import { Office } from '../../classes/offices';

@Component({
  selector: 'app-edit-card',
  templateUrl: './edit-card.component.html',
  styleUrls: ['./edit-card.component.css']
})
export class EditCardComponent implements OnInit {

  card: Card = {
    id: "",
    cardNo: undefined,
    location: "",
    cardType: "",
    validThru: 0,
    validFrom: 0,
    cardAvailable: true
  };
  validFrom = "";
  validThru = "";
  errorMessage = "";
  types: string[] = []
  offices: Office[];

  constructor(private route: ActivatedRoute, private _router: Router, private _cardService: CardService, private _officeService: OfficeService) { }


  /**
   * gets card details with id from the url.
   * and constructs card object.
   */
  ngOnInit() {
    this.getCard();
    this.getTypes();
    this.getLocations();
    
  }

  getCard(){
    this.route.params.subscribe(params => {
      this._cardService.getSingleCard(params['id']).subscribe(
        data => {
          this.validFrom = data['validFrom'].substring(0, 10)
          this.validThru = data['validThru'].substring(0, 10)
          this.card ={
            id: data['id'],
            cardNo: data['cardNo'],
            location: data['officeId'],
            cardType:  data['cardType'],
            validThru: data['validThru'],
            validFrom: data['validFrom'].replace('-', '/'),
            cardAvailable:  data['cardAvailable']
          };
        },
        err => {
          if (err['status'] == 401){
            this._router.navigate(['/auth-error']);
          }else{
            this.showError(err);
          }
        })
        
    })
  }

  getTypes(){
    this._cardService.getTypes().subscribe(
      data => {
        
        this.types = [];
        Object.keys(data).forEach(key => {
          this.types.push(data[key]);
        });
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
    });
  }

  /**
   * Converts date to match input required format
   * @param date string of the date that needs to be converted
   * @returns converted date
   */
  convertDate(date: string) {
    let dateSplit = date.split('-');
    if (dateSplit[1].length == 1) {
      dateSplit[1] = "0" + dateSplit[1]
    }
    if (dateSplit[0].length == 1) {
      dateSplit[0] = "0" + dateSplit[1]
    }
    let newDate = dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];
    return newDate;
  }

  /**
   * sends update request to server with updated card details.
   */
  onSubmit() {

   // this.card.validFrom = new DatePipe('en-US').transform(this.validFrom, 'dd-MM-yyyy')
    //this.card.validThru = new DatePipe('en-US').transform(this.validThru, 'dd-MM-yyyy')
    this.card.validThru = new Date(new DatePipe('en-US').transform(this.validThru, 'MM-dd-yyyy')+" 12:00:00").getTime();
    this.card.validFrom = new Date(new DatePipe('en-US').transform(this.validFrom, 'MM-dd-yyyy')+" 12:00:00").getTime();
    if (this.card.cardNo != undefined && this.card.cardType != "" && this.card.location != "" && this.validThru != "") {
      
      this._cardService.putCard(this.card).subscribe(
        data => {
          this._router.navigate(['/cards']);
        },
        err => {
          this.showError(err);
        }

      )

    }
  }

  /**
   * shows error notification if it occurs in any of the requests
   * @param err error object
   */
  showError(err){
    console.log(err)
    let element = document.getElementById('error-alert');
    element.style.display = "block";
    if(err['error']['error']){
      this.errorMessage = err['error']['error'] + ", try again later";
    }else {
      this.errorMessage = err['error'] + ", try again later";
    }
  }

}
