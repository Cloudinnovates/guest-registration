import { Component, OnInit, ViewChild } from '@angular/core';
import{EventService}  from '../../services/event.service';
import{Event}  from '../../classes/event';
import{Router}  from '@angular/router';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';


@Component({
  selector: 'app-pastevent',
  templateUrl: './pastevent.component.html',
  styleUrls: ['./pastevent.component.css']
})
export class PasteventComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  private events:Event[];
  private searchTerm: String;

  dataSource;

  private displayedColumns = ['eventName', 'manager', 'location', 'dateFrom',
                      'dateTo', 'participantNo', 'eventType', 'operations'];

  constructor(private _eventService:EventService, private _router:Router) { }



  ngOnInit() {
      this._eventService.getPastEvents().subscribe(results => {
        if (!results) {
          return;
        }
        this.dataSource = new MatTableDataSource(results);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      })
  }


  search(filterValue: string) {
    this._eventService.getSearchedEvents(filterValue).subscribe(results => {
      if (!results) {
        return;
      }
      this.dataSource = new MatTableDataSource(results);
    },
    err => {
      if (err['status'] == 401){
        this._router.navigate(['/auth-error']);
      }else{
        console.error(err);
      }
    }
  )

    if(!filterValue) this.ngOnInit();
  }

  deleteEvent(event) {
      this._eventService.deleteEvent(event.id).subscribe((results)=>{
          this.events.splice(this.events.indexOf(event),1);
      },err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      });
      this.dataSource.data = this.dataSource.data.filter(i => i !== event)
  }

  updateEvent(event){
     this._eventService.setter(event);
     this._router.navigate(['/event-form']);

  }

  newEvent(){
   let event = new Event();
    this._eventService.setter(event);
     this._router.navigate(['/event-form']);

  }


  clickMethod(event) {
     if(confirm("Are you sure you want to delete event: " + event.eventName + "?")) {
       this.deleteEvent(event);
     }

  }

}
