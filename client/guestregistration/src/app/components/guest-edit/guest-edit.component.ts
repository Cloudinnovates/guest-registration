import { Component, OnInit, ViewChild } from '@angular/core';   
import{Router}  from '@angular/router';
import {FormControl} from '@angular/forms';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material'; 

import { Observable } from 'rxjs';  
import { startWith, map } from 'rxjs/operators'; 
import{Guest}  from '../../classes/guest';
import { Card } from '../../classes/card';
import{GuestService}  from '../../services/guest.service';
import { CardService } from '../../services/card.service';
import { AgentService } from '../../services/agent.service';

export interface Agent { 
  name: string;
}

@Component({
  selector: 'app-guest-edit',
  templateUrl: './guest-edit.component.html',
  styleUrls: ['./guest-edit.component.css']
})
export class GuestEditComponent implements OnInit { 

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
 
  private guest: Guest;
  private card: Card;
  private cards: Card[];
  private agent: Agent; 

  private agents: any[] = []; 
  myControl = new FormControl();
  dataSource;     
  

  constructor(private _guestService: GuestService,private _router:Router, 
              private _cardService: CardService, private _agentService: AgentService) { }   
                

  ngOnInit() {
    this.guest=this._guestService.getterGuest();
    this.getCardNumbers();
    this.getAgents();   

  }

  processForm(){
    if(this.guest.id==undefined){
       this._guestService.createGuest(this.guest).subscribe((guest)=>{
         this._router.navigate(['/guest-registry']);
       },
       err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      });
    } else{
       this._guestService.updateGuest(this.guest).subscribe((guest)=>{   
         this._router.navigate(['/guest-registry']);
       },err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      });
    }
  }   

  getCardNumbers(){
    this._guestService.getCards().subscribe(cards => {  
      this.cards = [];
      Object.keys(cards).forEach(key => {
        this.cards.push(cards[key]);
      })
    },
    err => {
      if (err['status'] == 401){
        this._router.navigate(['/auth-error']);
      }
    });
  }      

  getAgents(){
    this._agentService.getAgents().subscribe(agents => {  
      this.agents = [];
      Object.keys(agents).forEach(key => { 
        this.agents.push(agents[key]); 
      })
    },
    err => {
      if (err['status'] == 401){
        this._router.navigate(['/auth-error']); 
      }
    });
  }

  displayFn(id) {
    if (!id) return '';
  
    let index = this.agents.findIndex(agent => agent.id === id);
    return this.agents[index].name + " " + this.agents[index].surname;      
  }   
  
  search(filterValue: string) {  
    this._guestService.getSearchedParticipants(filterValue).subscribe(results => {
      if (!results) {
        return;
      }
      this.dataSource = new MatTableDataSource(results);
      console.log(results);

      this.agents = [];
      Object.keys(results).forEach(key => {  
        let agent = {
          id: results[key]['id'],
          name: results[key]['name'],
          surname: results[key]['surname']  
        };
        this.agents.push(agent); 
      }) 
      
    },
      err => {
        if (err['status'] == 401) {
          this._router.navigate(['/auth-error']);
        } else {
          console.error(err);
        }
      }
    )

    if (!filterValue) this.ngOnInit();  
  }   
  





}       

