import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { GeneralService } from "../../services/general.service";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {
  @ViewChild('map',{static: false}) mapElement!: ElementRef;
  map!: google.maps.Map;
  marker!: google.maps.Marker;

  mapOptions!: google.maps.MapOptions

  constructor(private generalService: GeneralService) { }

  ngOnInit(): void {

  }
  mapInit() {
    const center = new google.maps.LatLng(1.2922, 103.7766);
    this.mapOptions = {
      center: center,
      zoom: 14,
      styles: [
            {
              featureType: "poi",
              stylers: [{ visibility: "off" }],
            },
            {
              featureType: "transit",
              elementType: "all",
              stylers: [{ visibility: "off" }],
            },
          ]
    };

    this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
    this.marker = new google.maps.Marker({
      position:center,
      map:this.map,
      title: "Mini Kitchen",
      icon: "/assets/icons/restaurant.png"
    });

    /*Info window*/
    const contentString =
      '<h2>Mini Kitchen</h2> <p>\n' +
      '    25 Heng Mui Keng Terrace<br>\n' +
      '    SG 119615, Singapore<br><br>\n' +
      '    <strong>Phone:</strong> +65 12345678<br>\n' +
      '    <strong>Email:</strong> mini-kitchen@yahoo.com<br>\n' +
      '  </p>' +
      '<a href="https://goo.gl/maps/ZDV9Y3Rm8CaxhcTT8">View in google map</a>'
    const infoWindow = new google.maps.InfoWindow({
      content:  contentString,
    });

    this.marker.addListener("click", () => {
        infoWindow.open({
        anchor: this.marker,
        map: this.map,
        shouldFocus: false,
      });
    });
  }
  ngAfterViewInit(): void {
    this.mapInit();
  }
}
