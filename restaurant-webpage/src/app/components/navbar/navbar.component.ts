import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router,
              private authService: AuthService) { }

  ngOnInit(): void {
  }

  bookTablePage() {
    this.router.navigate(['book-table']).then()
  }

  accountPage() {
    let accessToken = localStorage.getItem('mini-kitchen-access');
    if (accessToken) this.router.navigate(['my-account']).then()
    else this.router.navigate(['login']).then()
    this.router.navigate(['my-account']).then()
  }
}
