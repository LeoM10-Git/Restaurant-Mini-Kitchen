import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { GeneralService } from "../../services/general.service";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-account-contact-us',
  templateUrl: './account-contact-us.component.html',
  styleUrls: ['./account-contact-us.component.css']
})
export class AccountContactUsComponent implements OnInit {
  form!: FormGroup;
  sent: boolean = false;
  constructor(private fb: FormBuilder,
              private service: GeneralService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      email: [''],
      phoneNumber: [''],
      message: ['', [Validators.required]]
    })
  }

  sendFeedback() {
    this.service.sendFeedback(this.form.value).then((response) => {
      if (response) {
        console.log(response);
        this.sent = true;
        this.form.reset();
      }
    })
  }
}
