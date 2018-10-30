import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-sing-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  username: string = "";
  password: string = "";
  errorMessage: string = "";
  constructor(private _authService: AuthService) { }


  ngOnInit() {
  }

  signUp() {
    this._authService.signUp(this.username, this.password).subscribe(
      data => {},
      err => {
        if(err['status'] != 201){
          let element = document.getElementById('error-alert');
          element.style.display = "block";
          this.errorMessage = "Error occured while trying to sign you in, username already exists"
        }else{
          alert("registration succesfull");
        }
        
      }
    )
  }

}
