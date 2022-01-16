import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { IMedia } from 'app/urbanix/media/media.model';
import { MediaService } from 'app/urbanix/media/media.service';
import { IPark } from '../park.model';
import { ParkService } from '../park.service';

@Component({
  selector: 'jhi-park-near-me',
  templateUrl: './park-near-me.component.html',
  styleUrls: ['./park-near-me-style.scss'],
})
export class ParkNearMeComponent implements OnInit {
  lat!: number;
  long!: number;

  parks?: IPark[] = [];
  parksInCircle?: IPark[] = [];
  radius = 20000;
  maxRadius = 20000;
  markerIcon = '../../../../content/images/ubx-location.png';
  account!: Account | null;

  constructor(protected parkService: ParkService, protected mediaService: MediaService, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.parks = [];
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
        this.radius = 50000;
        this.maxRadius = 50000;
      }
      navigator.geolocation.getCurrentPosition(position => {
        this.lat = position.coords.latitude;
        this.long = position.coords.longitude;
        this.getParks();
      });
    });
  }
  getParks(): void {
    this.parkService.getParksByDistance(this.radius, this.lat, this.long).subscribe((res: HttpResponse<IPark[]>) => {
      this.parks = res.body!;

      this.parks.forEach(park => {
        park.modalPreview = '../../../../content/images/no-image.png';
        this.mediaService.query({ 'parkId.equals': park.id }).subscribe((resMedia: HttpResponse<IMedia[]>) => {
          if (resMedia.body?.length) {
            park.modalPreview = resMedia.body[0].filePath ? resMedia.body[0].filePath : '../../../../content/images/no-image.png';
          }
          this.showHideMarkers();
        });
      });
    });
  }

  changeRadius(event: any): void {
    if (event > this.radius) {
      if (event < this.maxRadius) {
        this.radius = event;
        this.getParks();
      } else {
        this.radius = this.maxRadius;
      }
    } else {
      this.radius = event;
      this.showHideMarkers();
    }
  }

  showHideMarkers(): void {
    this.parksInCircle = [];
    Object.values(this.parks!).forEach(park => {
      park.distance = this.getDistance(park.latitude!, park.longtitude!);
      park.isShown = park.distance <= this.radius;
      if (park.isShown) {
        this.parksInCircle?.push(park);
      }
    });
  }

  getDistance(lat: number, long: number): number {
    const from = new google.maps.LatLng(lat, long);
    const to = new google.maps.LatLng(this.lat, this.long);

    return google.maps.geometry.spherical.computeDistanceBetween(from, to);
  }
}
