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
  selector: 'jhi-equipement-view',
  templateUrl: './equipement-view.component.html',
  styleUrls: ['./equipement-view.scss'],
})
export class EquipementViewComponent implements OnInit {
  equipement: IEquipement = {};
  park: IPark = {};
  images: IMedia[] = [];

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
    if (this.equipement.park) {
      this.parkService
        .find(this.equipement.park.id!)
        .pipe(
          filter((mayBeOk: HttpResponse<IPark>) => mayBeOk.ok),
          map((response: HttpResponse<IPark>) => response.body)
        )
        .subscribe((resPark: IPark | null) => (this.park = resPark!));
    }
  }

  loadMedia(): void {
    this.mediaService.query({ 'equipementId.equals': this.equipement.id }).subscribe((resMedia: HttpResponse<IMedia[]>) => {
      if (resMedia.body?.length) {
        this.images = resMedia.body.filter(media => media.authType === 'LEARN');
      }
    });
  }
}
