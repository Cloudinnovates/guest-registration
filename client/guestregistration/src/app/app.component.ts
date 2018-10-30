import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  isLoggedIn: boolean= false;
translate: TranslateService;
  constructor(private _authService: AuthService, private _router: Router,translate: TranslateService ){
    this.isLogged();
    this.translate = translate;
    translate.setDefaultLang('en');
  }

  isLogged(){
    this._authService.checkCurrentUser().subscribe(
      data => {
        this.isLoggedIn = true
      },
      err => {
        this.isLoggedIn = false
      }
    );
  }

  logOut(){
    console.log("in logout")
    this._authService.logOut().subscribe(
      data => {
        console.log(data);
      }
    );
    this.isLoggedIn = false
    this._router.navigate(['/login']);
  }

  switchLanguage(language: string) {
    this.translate.use(language);
  }
}
