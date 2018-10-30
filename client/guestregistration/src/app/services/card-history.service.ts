import { Injectable } from '@angular/core';
import{Http, Response, Headers, RequestOptions} from '@angular/http';
import{Observable} from 'rxjs';
import{ Card }  from '../classes/card';   
import { map } from 'rxjs/operators';
import {catchError } from 'rxjs/operators';



@Injectable()
export class CardHistoryService { 
  private baseUrl:string='http://localhost:8080/api';
  private headers = new Headers({'Content-Type':'application/json'});
  private options = new RequestOptions({headers:this.headers,
    withCredentials: true });
  private card = new Card();  


  constructor(private _http:Http) { }


  getCards(){
    return this._http.get(this.baseUrl+'/card-history',this.options)       
    .pipe(map((response:Response)=>response.json(),catchError(this.errorHandler)))

  }

  getCardsById(id:String){  
    return this._http.get(this.baseUrl+'/card-history/'+ id, this.options).pipe(map((response:Response)=>response.json()) 
    ,catchError(this.errorHandler))

  } 

  getSearchedEvents(eventName:String){
    return this._http.get(this.baseUrl+'/searched-events/'+ eventName, this.options).pipe(map((response:Response)=>response.json())
    ,catchError(this.errorHandler))

  }

  getCard(id:string){            
    return this._http.get(this.baseUrl+'/event/'+id,this.options).pipe(map((response:Response)=>response.json()),
    catchError(this.errorHandler));
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

  setter(card:Card){
     this.card = card;
   }

  setterCard(card:Card){
    this.card = card; 
  }

  getter(){
    return this.card;   
  }

  getterCard(){ 
    return this.card;   
  }

  

}
