import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../services/auth.service";
import { Router } from "@angular/router";
import { EMPTY, throwError } from "rxjs";
import { error } from "@angular/compiler/src/util";
import { HttpResponse } from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  rememberMeChecked = false;
  form!: FormGroup;
  incorrect = false;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    })
  }

  login(): void {
    const email = this.form.get('email')?.value;
    const password = this.form.get('password')?.value;
    this.authService.login(email, password).then(
      (response) => {
          this.authService.setAccessToken(response[ 'access_token' ]);
          this.authService.setRefreshToken(response[ 'refresh_token' ]);

          this.router.navigate(['/my-account']).then(() => {
            /*If user choose to remember me, provide a long time expired token*/
            /*Clear the token when logout, next login, if not check to remember me, keep short time expired token*/
            if ( this.rememberMeChecked ) {
              this.authService.getRememberMeToken()
            }
          })
          return EMPTY;
      })
      .catch((error: any) => {
      this.authService.logout();
      this.incorrect = true;
      return throwError(() => new Error(error.message));
    })

  }

  rememberMe() {
    this.rememberMeChecked=!this.rememberMeChecked;
    console.log(this.rememberMeChecked);
  }
}
