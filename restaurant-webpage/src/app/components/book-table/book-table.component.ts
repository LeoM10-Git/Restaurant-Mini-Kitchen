import { Component, OnInit} from '@angular/core';
import { Booking } from "../../models/booking.model";
import { GeneralService } from "../../services/general.service";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-book-table',
  templateUrl: './book-table.component.html',
  styleUrls: ['./book-table.component.css']
})
export class BookTableComponent implements OnInit {

  constructor(private service: GeneralService,
              private router: Router,
              private authService: AuthService) { }

  ngOnInit(): void {
        if (!this.authService.getAccessToken()) {
          this.router.navigate(['login']).then()
        }
    }

  bookTable(booking: Booking) {
    this.service.bookTable(booking).then((response) => {
      console.log(response)
    });
    this.router.navigate(['/my-account']).then()
  }
}
