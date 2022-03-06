import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { User } from "../../models/user.model";
import { AuthService } from "../../services/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  form!: FormGroup;
  registered: boolean = true;
  isEmail: boolean = true;

  constructor(private fb:FormBuilder,
              private authService:AuthService,
              private router:Router) { }

  ngOnInit(): void {
    this.authService.removeSession();
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      email: ['',[Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    })
  }

  signup() {
    /*To reset the value if the page is not reload*/
    this.registered = true;
    this.isEmail = true;
    const user = this.form.value as User;
    this.authService.signup(user).then((response: any) => {
      if ( response[ 'message' ] === "registered" ) {
        this.registered = true;
        const email = user.email
        const password = user.password
        this.authService.login(email, password).then((response) => {
          this.authService.setAccessToken(response[ 'access_token' ])
          this.router.navigate(['my-account']).then()
        })
      }
      else if (response[ 'message' ] === "invalid"){
        this.isEmail = false
      }
      else if (response[ 'message' ] === "failed") {
        this.registered = false;
      }
    })
  }
}
