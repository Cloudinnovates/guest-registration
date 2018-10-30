import { Injectable } from '@angular/core';
import { Employee } from '../classes/employee';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'}),
  withCredentials: true
};

@Injectable()
export class EmployeeService {
  private baseUrl = 'http://localhost:8080';

  private messageSource = new BehaviorSubject<string>("default message");
  currentMessage = this.messageSource.asObservable()

  constructor(private http: HttpClient) {}

  changeMessage(editing_employee_id: string) {
    this.messageSource.next(editing_employee_id);
  }

  getAllEmployees() {
    return this.http.get(this.baseUrl + '/api/employees/getAll', httpOptions);
  }

  getEmployees(id: string) {
    return this.http.get(this.baseUrl + '/api/employees/getAll/' + id, httpOptions);
  }

  getEmployee(id: string) {
    return this.http.get(this.baseUrl + '/api/employees/' + id, httpOptions);
  }

  createEmployee(employeeData: Object): Observable<Object> {
    return this.http.post(this.baseUrl + '/api/employees/create', employeeData, httpOptions);
  }

  updateEmployee(employeeData: Employee) {
    return this.http.put(this.baseUrl + '/api/employees/update/' + employeeData.id, employeeData, httpOptions)
  }

  deleteEmployee(id: string): Observable<{}> {
    return this.http.delete(this.baseUrl + '/api/employees/delete/' + id, httpOptions);
  }

  findEmployees(officeId: string, text: string) {
    return this.http.get(this.baseUrl + '/api/employees/find/' + officeId + '/'  + text, httpOptions);
  }

  findEmployeeInFullList(text: string) {
    return this.http.get(this.baseUrl + '/api/employees/find/' + text, httpOptions);
  }

  checkEmployee(agentId: string) {
    return this.http.get(this.baseUrl + '/api/employees/check/' + agentId, httpOptions);
  }

}
