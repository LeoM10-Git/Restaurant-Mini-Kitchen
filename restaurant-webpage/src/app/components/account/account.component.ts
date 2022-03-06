import { Component, OnInit } from '@angular/core';
import { AccountService } from "../../services/account.service";
import { AuthService } from "../../services/auth.service";
import { User } from "../../models/user.model";
import { Router } from "@angular/router";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit{
  name!: string;

  constructor(private accountService: AccountService,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    if (this.authService.getAccessToken()){
      this.accountService.getAccountInfo().then((user: User) => {
        this.name = user.name;
      })
        .catch((error: any) => {
          this.authService.logout()
        })
    }else this.router.navigate(['/login']).then()
  }
}


