import { Component, OnInit } from '@angular/core';
import { FAQ } from "../../models/faq.model";
import { GeneralService } from "../../services/general.service";

@Component({
  selector: 'app-frequently-asked-questions',
  templateUrl: './frequently-asked-questions.component.html',
  styleUrls: ['./frequently-asked-questions.component.css']
})
export class FrequentlyAskedQuestionsComponent implements OnInit {
  faqs: FAQ[] = []


  constructor(private service: GeneralService) {
  }

  ngOnInit(): void {
    this.service.getFAQ().then( (FAQs: FAQ[]) => {
      this.faqs = FAQs;
    })
  }
}
