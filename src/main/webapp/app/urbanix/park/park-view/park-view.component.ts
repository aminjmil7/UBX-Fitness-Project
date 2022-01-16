import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEquipement } from '../../equipement/equipement.model';
import { EquipementService } from '../../equipement/equipement.service';
import { IMedia } from '../../media/media.model';
import { MediaService } from '../../media/media.service';
import { IPark } from '../park.model';

@Component({
  selector: 'jhi-park-view',
  templateUrl: './park-view.component.html',
  styleUrls: ['./park-view-style.scss'],
})
export class ParkViewComponent implements OnInit {
  park: IPark = {};
  equipements?: IEquipement[] = [];

  constructor(
    protected equipementService: EquipementService,
    protected activatedRoute: ActivatedRoute,
    protected mediaService: MediaService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ park }) => {
      this.park = park;
      this.park.distance = 0;
      this.park.distanceUnit = 'm';
      this.park.modalPreview = '../../../../content/images/no-image.png';
      this.loadMedia();
      this.loadEquipements();

      navigator.geolocation.getCurrentPosition(position => {
        const lat = position.coords.latitude || this.park.latitude;
        const long = position.coords.longitude || this.park.longtitude;
        this.park.distance = this.getDistance(lat!, long!);

        if (this.park.distance >= 1000) {
          this.park.distance = Number((this.park.distance / 1000).toFixed(0));
          this.park.distanceUnit = 'km';
        } else {
          this.park.distance = Number(this.park.distance.toFixed(0));
          this.park.distanceUnit = 'm';
        }
      });
    });
  }

  loadMedia(): void {
    this.mediaService.query({ 'parkId.equals': this.park.id }).subscribe((resMedia: HttpResponse<IMedia[]>) => {
      if (resMedia.body?.length) {
        this.park.modalPreview = resMedia.body[0].filePath ? resMedia.body[0].filePath : '../../../../content/images/no-image.png';
      }
    });
  }

  getDistance(lat: number, long: number): number {
    const from = new google.maps.LatLng(this.park.latitude!, this.park.longtitude!);
    const to = new google.maps.LatLng(lat, long);

    return google.maps.geometry.spherical.computeDistanceBetween(from, to) || 0;
  }

  loadEquipements(): void {
    this.equipements = [];
    this.equipementService
      .query({ 'parkId.equals': this.park.id, 'verified.equals': true })
      .subscribe((res: HttpResponse<IEquipement[]>) => {
        if (res.body) {
          this.equipements = res.body;
          this.equipements.forEach(equipement => {
            equipement.modalPreview = '../../../../content/images/no-image.png';
            this.mediaService.query({ 'equipementId.equals': equipement.id }).subscribe((resMedia: HttpResponse<IMedia[]>) => {
              if (resMedia.body?.length) {
                equipement.modalPreview = resMedia.body[0].filePath ? resMedia.body[0].filePath : '../../../../content/images/no-image.png';
              }
            });
          });
        }
      });
  }
}
