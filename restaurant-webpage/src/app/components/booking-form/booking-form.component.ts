import { Component, ElementRef, Input, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { Router } from "@angular/router";
import { BsDatepickerConfig } from "ngx-bootstrap/datepicker";
import { GeneralService } from "../../services/general.service";
import { Booking } from "../../models/booking.model";
import { Subject } from "rxjs";
import { BookingSession } from "../../models/booking-session.model";
import { Session } from "../../models/session.model";

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.css']
})
export class BookingFormComponent implements OnInit {

  form!: FormGroup;
  bsValue: Date = new Date();


  datePickerConfig: Partial<BsDatepickerConfig>= Object.assign({},
                                                    { showWeekNumbers: false,
                                                      dateInputFormat: 'YYYY-MM-DD',
                                                      bsValue: this.bsValue,
                                                      customTodayClass: "pick-today"
                                                    }
                                              );
  @Input()
  title: string = "Make your reservation";
  @Input()
  buttonText: string = "Book Now";
  @Input()
  booking?: Booking;

  @Output()
  onSubmit: Subject<Booking> = new Subject<Booking>();

  sessions!: Session[]
  bookingSessions!: BookingSession[]

  constructor(private router: Router,
              private fb: FormBuilder,
              private datePipe:DatePipe,
              private service: GeneralService) { }

  ngOnInit(): void {
    this.service.getBookingSession().then((bookingSession) => {
      this.bookingSessions = bookingSession;
      this.datePickerConfig.minDate = new Date(bookingSession[0].date);
      this.datePickerConfig.maxDate = new Date(bookingSession[bookingSession.length -1].date);

    })

    this.form = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.minLength(8), Validators.pattern("^[0-9]*$ || +"),]],
      guestNumber: ['', [Validators.required, Validators.maxLength(1), Validators.pattern("^[0-9]*$"),]],
      adultNumber: ['', [Validators.required, Validators.maxLength(1), Validators.pattern("^[0-9]*$"),]],
      childrenNumber: ['', [Validators.required, Validators.maxLength(1), Validators.pattern("^[0-9]*$"),]],
      date: ['', [Validators.required]],
      session: ['', [Validators.required]]
    })
  }

    ngOnChanges(changes: SimpleChanges) {
      this.booking = changes['booking'].currentValue
      /*Reload the session time inside the modal,
      if without this, the selection of session will not show, unless change the time, to load*/
      const savedDate = this.booking?.date
      if (this.bookingSessions?.length > 0) {
        for ( let bookingSession of this.bookingSessions ) {
          if (savedDate == bookingSession.date) {
            this.sessions = bookingSession.sessions.filter(session => session.availableTables>0)
          }
        }
      }

      this.form = this.fb.group({
        name: [this.booking?.name, [Validators.required]],
        email: [this.booking?.email, [Validators.required, Validators.email]],
        phoneNumber: [this.booking?.phoneNumber, [Validators.required, Validators.minLength(8), Validators.pattern("^[0-9]*$ || +"),]],
        guestNumber: [this.booking?.guestNumber, [Validators.required, Validators.maxLength(1), Validators.pattern("^[0-9]*$"),]],
        adultNumber: [this.booking?.adultNumber, [Validators.required, Validators.maxLength(1), Validators.pattern("^[0-9]*$"),]],
        childrenNumber: [this.booking?.childrenNumber, [Validators.required, Validators.maxLength(1), Validators.pattern("^[0-9]*$"),]],
        date: [this.booking?.date, [Validators.required]],
        session: [this.booking?.session, [Validators.required]]
      })
    }


  submitForm() {
    const date = this.datePipe.transform(this.form.get('date')?.value, 'yyyy-MM-dd') as string
    if (this.booking?.bookingId){

      const updatedBooking = {
        bookingId: this.booking.bookingId,
        name: this.form.get('name')?.value,
        email: this.form.get('email')?.value,
        phoneNumber: this.form.get('phoneNumber')?.value,
        guestNumber: this.form.get('guestNumber')?.value,
        adultNumber: this.form.get('adultNumber')?.value,
        childrenNumber: this.form.get('childrenNumber')?.value,
        date: date,
        session: this.form.get('session')?.value
      } as Booking
      this.onSubmit.next(updatedBooking)
    } else {
      const updatedBooking = this.form.value
      updatedBooking.date = date
      this.onSubmit.next(updatedBooking)
    }
  }

  changeDate(event: any) {
      let savedDate;
      for ( let bookingSession of this.bookingSessions ) {
        savedDate = new Date(bookingSession.date);

        if ( event.getDate() == savedDate.getDate() ) {
            this.sessions = bookingSession.sessions.filter(session => session.availableTables>0)
          }
        }
  }
}
