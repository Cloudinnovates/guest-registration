import { Component, OnInit, ViewChild } from '@angular/core';
import{CardHistoryService}  from '../../services/card-history.service';   
import{CardInfo}  from '../../classes/cardInfo';   
import{Router}  from '@angular/router';
import { Observable } from 'rxjs';
import {DataSource} from '@angular/cdk/collections';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material'; 
import { Card } from 'src/app/classes/card';

@Component({
  selector: 'app-card-history',
  templateUrl: './card-history.component.html',
  styleUrls: ['./card-history.component.css']     
})

export class CardHistoryComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  private cardInfos: CardInfo[];  
  private card: Card; 
  private cardNumber: Number; 
  private searchTerm: String;   
  private message: String = "This card was never assigned to any person";   

  dataSource;
  private displayedColumns = ['cardNumber', 'firstName', 'lastName', 'holderType', 'location',
                              'issued', 'returned'];   


  constructor(private _cardHistoryService: CardHistoryService, private _router: Router) { }    

  ngOnInit() {
    
    this.card = this._cardHistoryService.getterCard();   
    
      this._cardHistoryService.getCardsById(this.card.id).subscribe(results => {  
        
      if (!results) { 
        return;
      }

      this.dataSource = new MatTableDataSource(results);  
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.message = ""; 

    },
      err => {
        if (err['status'] == 401) {
          this._router.navigate(['/auth-error']);
        } else {
          console.error(err);
        }
      }

    )
  }



}  
