import { Component, OnInit, Inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { Config } from 'protractor';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string = "";
  password: string = "";
  errorMessage = "";

  constructor(@Inject(AppComponent) private parent: AppComponent, private _router: Router, private _authService: AuthService) {
    this._authService.checkCurrentUser().subscribe(
      data => {
        this._router.navigate(['/']);
      }
    )
  }

  ngOnInit() {
  }

  signIn() {
    this._authService.logIn(this.username, this.password).subscribe(
      data => {
        this._router.navigate(['/']);
        this.parent.isLogged();
      },
      err => {
        console.log(err)
        let element = document.getElementById('error-alert');
        element.style.display = "block";
        this.errorMessage = "Error occured while trying to log you in, check your credentials and try again"
      }
    )
  }

}
