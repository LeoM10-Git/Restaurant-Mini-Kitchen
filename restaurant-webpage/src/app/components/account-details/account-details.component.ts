import { Component, OnInit } from '@angular/core';
import { User } from "../../models/user.model";
import { AccountService } from "../../services/account.service";
import { AuthService } from "../../services/auth.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { GeneralService } from "../../services/general.service";

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
  name!: string;
  email!: string;
  password!: string;
  form!: FormGroup;
  info = false;
  updated = false;

  constructor(private accountService: AccountService,
              private authService: AuthService,
              private fb: FormBuilder,
              private service: GeneralService) { }

  ngOnInit(): void {
    this.accountService.getAccountInfo().then((user: User) => {
      this.form = this.fb.group({
        name: [user.name, [Validators.required]],
        email: [user.email, [Validators.required]],
        currentPassword: [''],
        newPassword: [''],
        confirmPassword: [''],
      })
    })
      .catch((error: any) => {
        this.authService.logout()
      })
  }

  updateAccount() {
    if (this.form.get('currentPassword')?.value && this.form.get('newPassword')?.value) {
      if (this.form.get('newPassword')?.value &&
        this.form.get('newPassword')?.value != this.form.get('confirmPassword')?.value) {
        console.log('true')
        this.info = true;
      } else {
        this.service.updateAccount(this.form.value).then((result: any) => {
          this.updated = result[ 'updated' ] !== 'false';
        })
      }
    }else {
      this.service.updateAccount(this.form.value).then((result: any) => {
        this.updated = result[ 'updated' ] !== 'false';
      })
    }
  }
}
