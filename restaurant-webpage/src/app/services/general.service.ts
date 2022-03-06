import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Booking } from "../models/booking.model";
import { lastValueFrom } from "rxjs";
import { ContactMessage } from "../models/contact-message.model";
import { UpdatedUser } from "../models/user-update.model";
import { FAQ } from "../models/faq.model";
import { BookingSession } from "../models/booking-session.model";

@Injectable({
  providedIn: 'root'
})
export class GeneralService {

  API_URL="/api/user/resources"

  constructor(private http: HttpClient) { }

  getFAQ() :Promise<FAQ[]>{
      return lastValueFrom(this.http.get("/api/user/FAQs")) as Promise<FAQ[]>
  }

  bookTable(booking: Booking): Promise<Booking>{
    return lastValueFrom(this.http.post(`${this.API_URL}/bookings`, booking)) as Promise<Booking>;
  }

  getBookings(): Promise<Booking[]>{
    return lastValueFrom(this.http.get(`${this.API_URL}/bookings`)) as Promise<Booking[]>;
  }

  sendFeedback(content: ContactMessage): Promise<any> {
    return lastValueFrom(this.http.post(`${this.API_URL}/contact-us`, content))
  }

  updateBooking(booking: Booking) {
    return lastValueFrom(this.http.put(`${this.API_URL}/bookings/update/${booking.bookingId}`, booking))
  }

  deleteBooking(bookingId: string) {
    return lastValueFrom(this.http.delete(`${this.API_URL}/bookings/delete/${bookingId}`))
  }

  updateAccount(userInfo: UpdatedUser) {
    return lastValueFrom(this.http.put(`${this.API_URL}/update/account-info`, userInfo))
  }

  getBookingSession() :Promise<BookingSession[]> {
    return lastValueFrom(this.http.get(`${this.API_URL}/booking/booking-session`)) as Promise<BookingSession[]>
  }
}
