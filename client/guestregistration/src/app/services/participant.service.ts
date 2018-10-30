import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

let httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  }),
  withCredentials: true
};
@Injectable({
  providedIn: 'root'
})
export class ParticipantService {

  constructor(private http: HttpClient) { }

  getAllParticipants(eventId: string){
    return this.http.get('http://localhost:8080/api/participants/'+eventId, httpOptions)
  }

  addParticipantToEvent(eventId: string, agentId: string){
    let data = {
      "eventId": eventId,
      "agentId": agentId
    }
    return this.http.post('http://localhost:8080/api/participants', data , httpOptions);
  }

  deleteParticipantfromEvent(id: string){
    return this.http.delete('http://localhost:8080/api/participants?id='+id, httpOptions);
  }

  getAgentsNotInEvent(eventId: string){
    return this.http.get('http://localhost:8080/api/participants/free/'+eventId, httpOptions)
  }

  findAgentsNotInEvent(eventId: string, text: string){
    return this.http.get('http://localhost:8080/api/participants/find?eventId='+eventId+'&text='+text, httpOptions)  
  }

  updateParticipant(participant){
    return this.http.put('http://localhost:8080/api/participants', participant , httpOptions); 
  }


} 


