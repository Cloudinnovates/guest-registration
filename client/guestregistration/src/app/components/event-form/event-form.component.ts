import { Component, OnInit } from '@angular/core';
import{Event}  from '../../classes/event';
import{Router}  from '@angular/router';
import{EventService}  from '../../services/event.service';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {
  private event:Event;

  constructor(private _eventService:EventService,private _router:Router) { }

  ngOnInit() {
    this.event=this._eventService.getter();
    this.event.private = true;
  }

  processForm(){
    if(this.event.id==undefined){
       this._eventService.createEvent(this.event).subscribe((event)=>{
         console.log(event);
         this._router.navigate(['/upcoming-events']);
       },
       err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      });
    } else{
       this._eventService.updateEvent(this.event).subscribe((event)=>{
         console.log(event);
         this._router.navigate(['/upcoming-events']);
       },err => {
        if (err['status'] == 401){
          this._router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      });
    }
  }

}
