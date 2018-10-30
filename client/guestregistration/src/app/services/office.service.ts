import { Injectable } from '@angular/core';
import { Office } from '../classes/offices';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'}),
  withCredentials: true
};

@Injectable()
export class OfficeService {
  private baseUrl = 'http://localhost:8080';

  private messageSource = new BehaviorSubject<string>("default message");
  currentMessage = this.messageSource.asObservable()

  constructor(private http: HttpClient) {}

  changeMessage(editing_office_id: string) {
    this.messageSource.next(editing_office_id);
  }

  getOffices() {
    return this.http.get(this.baseUrl + '/api/office_register/getAll', httpOptions);
  }

  getOffice(id: string): Observable<{}> {
    return this.http.get(this.baseUrl + '/api/office_register/' + id, httpOptions);
  }

  createOffice(officeData: Object): Observable<Object> {
    return this.http.post(this.baseUrl + '/api/office_register/create', officeData, httpOptions);
  }

  updateOffice(officeData: Office) {
    return this.http.put(this.baseUrl + '/api/office_register/update/' + officeData.id, officeData, httpOptions)
  }

  deleteOffice(id: string): Observable<{}> {
    return this.http.delete(this.baseUrl + '/api/office_register/delete/' + id, httpOptions);
  }

  findOffices(text: string) {
    return this.http.get(this.baseUrl + '/api/office_register/find/' + text, httpOptions);
  }

}
