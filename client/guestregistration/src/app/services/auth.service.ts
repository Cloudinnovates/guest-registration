import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  }),
  withCredentials: true
}

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  constructor(private http: HttpClient) { }

  logIn(username: string, password: string) {
    let data = {
      "username": username,
      "password": password
    }
    return this.http.post('http://localhost:8080/api/auth/login', data, httpOptions)
      .pipe(map(response => {
        localStorage.setItem('currentUser', "accessToken");
        return response;
      }));
  }

  logOut() {
    localStorage.removeItem('currentUser');
    return this.http.get('http://localhost:8080/api/auth/logout', httpOptions)
  }

  signUp(username: string, password: string) {
    let data = {
      "username": username,
      "password": password
    }
    return this.http.post('http://localhost:8080/api/auth/signup', data, httpOptions);
  }

  checkCurrentUser(){
    return this.http.get('http://localhost:8080/api/auth/current_user', httpOptions);
  }

}
