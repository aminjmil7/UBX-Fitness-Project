import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IMedia } from 'app/urbanix/media/media.model';
import { MediaService } from 'app/urbanix/media/media.service';
import { IPark } from 'app/urbanix/park/park.model';
import { ParkService } from 'app/urbanix/park/park.service';
import { filter, map } from 'rxjs/operators';
import { IEquipement } from '../equipement.model';

@Component({
  selector: 'jhi-equipement-service',
  templateUrl: './equipement-service.component.html',
  styleUrls: ['./equipement-service.scss'],
})
export class EquipementServiceComponent implements OnInit {
  equipement: IEquipement = {};
  park: IPark = {};
  images: IMedia[] = [];
  services: IMedia[] = [];

  constructor(protected parkService: ParkService, protected activatedRoute: ActivatedRoute, protected mediaService: MediaService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipement }) => {
      this.equipement = equipement;
      this.images.push({ filePath: '../../../../content/images/no-image.png' });
      this.getPark();
      this.loadMedia();
    });
  }

  getPark(): void {
    this.parkService
      .find(this.equipement.park!.id!)
      .pipe(
        filter((mayBeOk: HttpResponse<IPark>) => mayBeOk.ok),
        map((response: HttpResponse<IPark>) => response.body)
      )
      .subscribe((resPark: IPark | null) => (this.park = resPark!));
  }

  loadMedia(): void {
    this.mediaService.query({ 'equipementId.equals': this.equipement.id }).subscribe((resMedia: HttpResponse<IMedia[]>) => {
      if (resMedia.body?.length) {
        this.images = resMedia.body.filter(media => media.authType === 'LEARN');
        this.services = resMedia.body.filter(media => media.authType === 'TECHFILE');
      }
    });
  }

  isImage(extension: string): boolean {
    const imageExtentions = ['JPEG', 'JPG', 'PNG', 'GIF', 'TIFF', 'JFIF']; // Array of image extention
    return imageExtentions.indexOf(extension.toLocaleUpperCase()) !== -1;
  }

  isVideo(extension: string): boolean {
    const imageExtentions = ['MP4', 'MOV', 'AVI']; // Array of videos extention
    return imageExtentions.indexOf(extension.toLocaleUpperCase()) !== -1;
  }
}
