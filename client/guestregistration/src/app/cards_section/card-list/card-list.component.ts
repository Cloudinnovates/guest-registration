import { Component, OnInit, ViewChild } from '@angular/core';
import { CardService } from '../../services/card.service';
import { Router } from '@angular/router';
import { MatPaginator, MatTableDataSource, MatSort } from '@angular/material';
import { OfficeService } from '../../services/office.service';
import { CardHistoryService } from '../../services/card-history.service';  

@Component({
  selector: 'app-card-list',
  templateUrl: './card-list.component.html',
  styleUrls: ['./card-list.component.css']
})
export class CardListComponent implements OnInit {


  cards: CardElement[] = [];
  availableCardsCount: number = 0;
  searchInputField: string = "";

  dataSource: MatTableDataSource<CardElement>;
  displayedColumns: string[] = ['cardNo', 'validFrom', 'validThru', 'office', 'cardType', 'availability', 'action']
  searchType: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _router: Router, private _cardService: CardService,
              private _cardHistoryService: CardHistoryService, private _officeService: OfficeService) {  
    this.searchType = this.displayedColumns[0];
    this.getCards();
  }

  ngOnInit() {

  }



  /**
   * receives all cards that are avilabe from the server,
   * and fills card array so that cars would be displayed 
   */
  getCards() {
    this._cardService.getCards().subscribe(
      data => {
        this.cards = [];
        Object.keys(data).forEach(key => {
          this._officeService.getOffice(data[key]['officeId']).subscribe(
            office => {
              let card = {
                id: data[key]['id'],
                cardNo: data[key]['cardNo'],
                location: office['name'],
                cardType: data[key]['cardType'],
                validThru: data[key]['validThru'].substring(0, 10),
                validFrom: data[key]['validFrom'].substring(0, 10),
                cardAvailable: data[key]['cardAvailable']
              }
              this.cards.push(card);
              this.getAvailableCardCount();
              this.dataSource = new MatTableDataSource(this.cards);
              this.dataSource.paginator = this.paginator;
              this.dataSource.sort = this.sort;
            },
            err => {
              console.log(err);
              if (err['status'] == 401){
                this._router.navigate(['/auth-error']);
              }
            }
          )

        })

      },
      err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }
      }
    );
  }

  /**
   * gets available card count and updates 
   * availableCardsCount variable
   */
  getAvailableCardCount() {
    this.availableCardsCount = 0;
    this.cards.forEach(card => {
      if (card.cardAvailable) {
        this.availableCardsCount++;
      }
    });
  }

  /**
   * called when delete button is pressed. sends delete 
   * request to server with card id, disables edit/delete buttons after 
   * sending request.
   */
  deleteButtonPressed(id) {
    this._cardService.deleteCard(id).subscribe(
      data => {
        this.getCards();
      },
      err => {
        console.log(err);
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }
      }
    );
  }

  /**
   * called when edit button is pressed. forwards user to
   *  new window (edit card page).
   */
  editButtonPressed(id) {
    this._router.navigate(['/cards/edit/' + id]);
  }

  
  /**
   * called when edit button is pressed. forwards user to
   *  new window (card history).
   */
  historyButtonPressed(row) {  
    this._cardHistoryService.setterCard(row);            
    this._router.navigate(['/card-history/']);  
  }

  /**
   * called when input changes in search input field.
   *  sends request to the server that returns all cards 
   * that contain searched phrase. cards array is updated.
   */
  searchInput() {
    this._cardService.findCards(this.searchInputField, this.searchType).subscribe(
      data => {
        
        if (Object.keys(data).length != 0) {
          this.cards = [];
          Object.keys(data).forEach(key => {
            this._officeService.getOffice(data[key]['officeId']).subscribe(
              office => {
                let card = {
                  id: data[key]['id'],
                  cardNo: data[key]['cardNo'],
                  location: office['name'],
                  cardType: data[key]['cardType'],
                  validThru: data[key]['validThru'].substring(0, 10),
                  validFrom: data[key]['validFrom'].substring(0, 10),
                  cardAvailable: data[key]['cardAvailable']
                }
                this.cards.push(card);
                this.getAvailableCardCount();
                this.dataSource = new MatTableDataSource(this.cards);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
              }
            )

          })
        } else {
          this.cards = []
          this.getAvailableCardCount();
          this.dataSource = new MatTableDataSource(this.cards);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      },
      err => {
          console.log(err);
          if (err['status'] == 401){
            this._router.navigate(['/auth-error']);
        }
      }
    );
  }



  // =----------------------------------------



}

export interface CardElement {
  id: string;
  cardNo: number;
  location: string;
  cardType: string;
  validThru: string;
  validFrom: string;
  cardAvailable: boolean;
}

