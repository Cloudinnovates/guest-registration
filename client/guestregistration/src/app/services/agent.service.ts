import { Injectable } from '@angular/core';
import { Agent } from '../classes/agent';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'}),
  withCredentials: true
};

@Injectable()
export class AgentService {
  private baseUrl = 'http://localhost:8080';

  private messageSource = new BehaviorSubject<string>("default message");
  currentMessage = this.messageSource.asObservable()

  constructor(private http: HttpClient) {}

  changeMessage(editing_agent_id: string) {
    this.messageSource.next(editing_agent_id);
  }

  getAgents() {
    return this.http.get(this.baseUrl + '/api/agent/getAll', httpOptions);
  }

  getAgent(id: string): Observable<{}> {
    return this.http.get(this.baseUrl + '/api/agent/' + id, httpOptions);
  }

  createAgent(agentData: Object): Observable<Object> {
    return this.http.post(this.baseUrl + '/api/agent/create', agentData, httpOptions);
  }

  updateAgent(agentData: Agent) {
    return this.http.put(this.baseUrl + '/api/agent/update/' + agentData.id, agentData, httpOptions)
  }

  deleteAgent(id: string): Observable<{}> {
    return this.http.delete(this.baseUrl + '/api/agent/delete/' + id, httpOptions);
  }

  findAgents(text: string) {
    return this.http.get(this.baseUrl + '/api/agent/find/' + text, httpOptions);
  }

  checkAgent(name: string, surname: string) {
    return this.http.get(this.baseUrl + '/api/agent/check/' + name + '/' + surname, httpOptions);
  }

}
