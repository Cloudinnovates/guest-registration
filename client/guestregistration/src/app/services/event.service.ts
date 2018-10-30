import { Injectable } from '@angular/core';
import{Http, Response, Headers, RequestOptions} from '@angular/http';
import{Observable} from 'rxjs';
import{Event}  from '../classes/event';
import { map } from 'rxjs/operators';
import {catchError } from 'rxjs/operators';



@Injectable()
export class EventService {
  private baseUrl:string='http://localhost:8080/api';
  private headers = new Headers({'Content-Type':'application/json'});
  private options = new RequestOptions({headers:this.headers,
    withCredentials: true });
  private event = new Event();


  constructor(private _http:Http) { }


  getEvents(){
    return this._http.get(this.baseUrl+'/events',this.options)
    .pipe(map((response:Response)=>response.json(),catchError(this.errorHandler)))

  }

  getPastEvents(){
    return this._http.get(this.baseUrl+'/past-events',this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))

  }

  getSearchedEvents(eventName:String){
    return this._http.get(this.baseUrl+'/searched-events/'+ eventName, this.options).pipe(map((response:Response)=>response.json())
    ,catchError(this.errorHandler))

  }

  getEvent(id:string){
    return this._http.get(this.baseUrl+'/event/'+id,this.options).pipe(map((response:Response)=>response.json()),
    catchError(this.errorHandler));
  }


  getEvento() {
    return this._http.get(this.baseUrl+'/events',this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))

  }

  deleteEvent(id:Number){
    return this._http.delete(this.baseUrl+'/event/'+id,this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))

  }


  createEvent(event:Event){
    return this._http.post(this.baseUrl+'/event',JSON.stringify(event),  this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))

  }

  updateEvent(event:Event){
    return this._http.put(this.baseUrl+'/event',JSON.stringify(event),  this.options).pipe(map((response:Response)=>response.json(),
    catchError(this.errorHandler)));
  }

  errorHandler(error:Response){
     return Observable.throw(error||"SERVER ERROR");
  }

  setter(event:Event){
     this.event=event;
   }

  getter(){
    return this.event;
  }

  getPublicCurrentEvents(){
    return this._http.get('http://localhost:8080/api/events/current_public', this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))
  }

  getCurrentEvents(){
    return this._http.get('http://localhost:8080/api/events/current', this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))
  }

  

}
