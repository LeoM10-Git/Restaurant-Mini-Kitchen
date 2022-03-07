import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from "./components/main/main.component";
import { ContactComponent } from "./components/contact/contact.component";
import { BookTableComponent } from "./components/book-table/book-table.component";
import {
  FrequentlyAskedQuestionsComponent
} from "./components/frequently-asked-questions/frequently-asked-questions.component";
import { LoginComponent } from "./components/login/login.component";
import { SignupComponent } from "./components/signup/signup.component";
import { AccountComponent } from "./components/account/account.component";
import { MenuComponent } from "./components/menu/menu.component";
import { AccountManageBookingComponent } from "./components/account-manage-booking/account-manage-booking.component";
import { AccountDetailsComponent } from "./components/account-details/account-details.component";
import { AccountContactUsComponent } from "./components/account-contact-us/account-contact-us.component";
import { PageGuardService } from "./services/page-guard.service";
import { AboutComponent } from "./components/about/about.component";

const routes: Routes = [
  {path: '', component: MainComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'book-table', component: BookTableComponent, canActivate: [ PageGuardService ]},
  {path: 'frequently-asked-questions', component:FrequentlyAskedQuestionsComponent},
  {path: 'login', component:LoginComponent},
  {path: 'signup',component:SignupComponent},
  {path: 'about', component:AboutComponent},
  {path: 'my-account', component:AccountComponent},
  {path: 'my-account/booking', component:AccountManageBookingComponent},
  {path: 'my-account/details', component:AccountDetailsComponent},
  {path: 'my-account/contact-us', component:AccountContactUsComponent, canActivate: [ PageGuardService ]},

  {path: 'menu', component:MenuComponent},
  {path:'**', redirectTo:'', pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
