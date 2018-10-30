import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Event } from '../../classes/event';
import { EventService } from '../../services/event.service';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import { diPublicInInjector } from '@angular/core/src/render3/di';
import{Router}  from '@angular/router';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  events: Event[] = []
  loggedIn: boolean = false;
  dataSource;
  

  publicColumns = ['eventName', 'manager', 'location', 'dateFrom',
  'dateTo', 'participantNo', 'eventType']
  privateColumns = ['eventName', 'manager', 'location', 'dateFrom',
  'dateTo', 'participantNo', 'eventType', 'operations']
  private displayedColumns = this.publicColumns;
  constructor(private _authService: AuthService,
    private _eventService: EventService,
    private _router: Router) { }

  ngOnInit() {
    this._authService.checkCurrentUser().subscribe(
      data => {
        this.displayedColumns = this.privateColumns
        this.loggedIn = true;
        this.getCurrentEvents()
      },
      err => {
        this.displayedColumns = this.publicColumns
        this.loggedIn = false;
        this.getPublicCurrentEvents()
      }
    )
  }

  getCurrentEvents(){
    this._eventService.getCurrentEvents().subscribe(
      data => {
        data.forEach(element => {
          this.events.push(element)
        });
        this.fillTable()
      },
      err => {
        console.log(err)
      }
    )
  }

  getPublicCurrentEvents(){
    this._eventService.getPublicCurrentEvents().subscribe(
      data => {
        data.forEach(element => {
          this.events.push(element)
        });
        this.fillTable()
      },
      err => {
        console.log(err)
      }
    )
  }

  fillTable(){
    this.dataSource = new MatTableDataSource(this.events);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  checkInButton(id: string){
    this._router.navigate(['/event-checkin/'+id])
  }
}
