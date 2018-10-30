import { Injectable } from '@angular/core';
import{Http, Response, Headers, RequestOptions} from '@angular/http';
import{Observable} from 'rxjs';
import{Guest}  from '../classes/guest';
import { map } from 'rxjs/operators';
import {catchError } from 'rxjs/operators';



@Injectable()
export class GuestService {
  private baseUrl:string='http://localhost:8080/api';
  private headers = new Headers({'Content-Type':'application/json'});
  private options = new RequestOptions({headers:this.headers,
    withCredentials: true});

  private guest = new Guest();


  constructor(private _http:Http) { }

  getGuests(){
    return this._http.get(this.baseUrl+'/guests',this.options)
    .pipe(map((response:Response)=>response.json(),catchError(this.errorHandler)))
  }

  getSearchedGuests(firstName:String){
    return this._http.get(this.baseUrl+'/searched-guests/'+ firstName, this.options).pipe(map((response:Response)=>response.json())
    ,catchError(this.errorHandler))

  }

  getSearchedParticipants(participantName:String){
    return this._http.get(this.baseUrl+'/searched-participants/'+ participantName, this.options).pipe(map((response:Response)=>response.json())
    ,catchError(this.errorHandler)) 
  } 

  getGuest(id:Number){
    return this._http.get(this.baseUrl+'/guest/'+id,this.options).pipe(map((response:Response)=>response.json()),
    catchError(this.errorHandler));
  }


  deleteGuest(id:Number){
    return this._http.delete(this.baseUrl+'/guest/'+id,this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))
  }


  createGuest(guest:Guest){
    return this._http.post(this.baseUrl+'/guest',JSON.stringify(guest),  this.options).pipe(map((response:Response)=>response.json()),catchError(this.errorHandler))

  }

  updateGuest(guest:Guest){
    return this._http.put(this.baseUrl+'/guest',JSON.stringify(guest),  this.options).pipe(map((response:Response)=>response.json(),
    catchError(this.errorHandler)));
  }

  errorHandler(error:Response){
     return Observable.throw(error||"SERVER ERROR");   
  }

  setterGuest(guest:Guest){
     this.guest=guest;
   }

  getterGuest(){
    return this.guest;
  }

  getCards(){
    return this._http.get(this.baseUrl+'/sorted-cards',this.options)
    .pipe(map((response:Response)=>response.json(),catchError(this.errorHandler)))
  }     


}
