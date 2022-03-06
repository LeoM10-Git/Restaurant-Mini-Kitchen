import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  starters: boolean = false;
  specialty: boolean = false;
  salads: boolean = false;

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.filterAll();
  }

  bookTablePage() {
    let accessToken = localStorage.getItem('access_token');
    if (accessToken) this.router.navigate(['book-table']).then()
    else this.router.navigate(['login']).then()
  }

  filterStarters() {
    this.starters = true;
    this.specialty = false;
    this.salads = false;
  }

  filterSpecialty() {
    this.specialty = true;
    this.starters = false;
    this.salads = false;
  }


  filterSalads() {
    this.salads = true;
    this.specialty = false;
    this.starters = false;
  }

  filterAll() {
    this.salads = true;
    this.specialty = true;
    this.starters = true;
  }
}
