import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking } from "../../models/booking.model";
import { GeneralService } from "../../services/general.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-account-manage-booking',
  templateUrl: './account-manage-booking.component.html',
  styleUrls: ['./account-manage-booking.component.css']
})
export class AccountManageBookingComponent implements OnInit {

  title: string = 'Update Reservation';
  buttonText: string = 'Update';
  bookingForm!: Booking;
  sortForHistoryBookings: boolean = false;
  historyBookings: Booking[] = []
  activeBookings: Booking[] = []
  bookings!: Booking[]

  imagePath = ["/assets/images/booking-card/image1.jpeg",
    "/assets/images/booking-card/image2.jpeg",
    "/assets/images/booking-card/image3.jpeg",
    "/assets/images/booking-card/image4.jpeg",
    "/assets/images/booking-card/image5.jpeg"];


  constructor(private service:GeneralService,
              private router:Router) {}

  ngOnInit(): void {
    this.service.getBookings().then(bookings => {
      let date = new Date()
      date.setDate(date.getDate() -1)
      this.bookings = bookings
        for (let booking of bookings) {
          if ( new Date(booking.date) < date) {
            console.log(new Date(booking.date))
            this.historyBookings.push(booking);
            console.log(booking)
          } else {
            this.activeBookings.push(booking);
          }
        }
    })
  }


  editBooking(booking: Booking) {
    this.bookingForm = booking
    console.log(booking)
  }

  updateBooking(booking: Booking) {
    this.service.updateBooking(booking).then((response) => {
      console.log(response)
      /*Because of the modal, need to reload the whole page*/
      this.reloadComponent();
    })
  }

  deleteBooking(bookingId: string) {
    this.service.deleteBooking(bookingId).then((response) => {
      this.reloadComponent();
    })
  }

  /*reload the page after update or delete*/
  reloadComponent() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['/my-account/booking']).then(() => {
      location.reload();
    })
  }

  sortActiveBooking() {
    this.sortForHistoryBookings = false;
  }

  sortForHistory() {
    this.sortForHistoryBookings = true;
  }
}
