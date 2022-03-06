import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MainComponent } from './components/main/main.component';
import { FooterComponent } from './components/footer/footer.component';
import { ContactComponent } from './components/contact/contact.component';
import { GoogleMapsModule } from "@angular/google-maps";
import { BookTableComponent } from './components/book-table/book-table.component';
import { FrequentlyAskedQuestionsComponent } from './components/frequently-asked-questions/frequently-asked-questions.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { AccountComponent } from './components/account/account.component';
import { MenuComponent } from './components/menu/menu.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { DatePipe } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BookingFormComponent } from './components/booking-form/booking-form.component';
import { BsDatepickerModule } from "ngx-bootstrap/datepicker";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { AccountDetailsComponent } from './components/account-details/account-details.component';
import { AccountManageBookingComponent } from './components/account-manage-booking/account-manage-booking.component';
import { AccountSidebarComponent } from './components/account-sidebar/account-sidebar.component';
import { AccountContactUsComponent } from './components/account-contact-us/account-contact-us.component';
import { TokenInterceptorService } from "./services/token-interceptor.service";
import { AboutComponent } from './components/about/about.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    MainComponent,
    FooterComponent,
    ContactComponent,
    BookTableComponent,
    FrequentlyAskedQuestionsComponent,
    LoginComponent,
    SignupComponent,
    AccountComponent,
    MenuComponent,
    BookingFormComponent,
    AccountDetailsComponent,
    AccountManageBookingComponent,
    AccountSidebarComponent,
    AccountContactUsComponent,
    AboutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    GoogleMapsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    BsDatepickerModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [DatePipe,
    {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptorService,
    multi: true,
  }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
