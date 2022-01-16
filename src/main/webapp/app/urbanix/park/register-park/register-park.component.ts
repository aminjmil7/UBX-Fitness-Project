import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMedia } from '../../media/media.model';
import { MediaService } from '../../media/media.service';
import { ParkService } from '../../park/park.service';
import { EquipementService } from '../../equipement/equipement.service';
import { IPark, Park } from '../park.model';
import { IEquipement } from 'app/urbanix/equipement/equipement.model';
import { AuthType } from 'app/urbanix/media/auth-type.model';

@Component({
  selector: 'jhi-register-park',
  templateUrl: './register-park.component.html',
  styleUrls: ['./register-park.component.scss'],
})
export class RegisterParkComponent implements OnInit {
  isSaving = false;

  urlFilesList: IMedia[] = [];

  editForm = this.fb.group({
    id: [],
    parkName: [],
    parkAddress: [],
    longtitude: [],
    latitude: [],
    verified: [],
  });
  longitude!: number;
  latitude!: number;
  equipement: IEquipement = {};
  currentAdresse = '';

  constructor(
    protected parkService: ParkService,
    protected mediaService: MediaService,
    protected equipementService: EquipementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.getLocation();
    this.activatedRoute.data.subscribe(({ equipement }) => {
      this.equipement = equipement || {};
    });
  }

  getLocation(): void {
    navigator.geolocation.getCurrentPosition(position => {
      this.longitude = position.coords.longitude;
      this.latitude = position.coords.latitude;
      this.geocodeLatLng();
    });
  }

  geocodeLatLng(): void {
    const geocoder = new google.maps.Geocoder();
    const latlng = { lat: this.latitude, lng: this.longitude };

    geocoder.geocode({ location: latlng }, response => {
      if (response.length) {
        this.currentAdresse = response[0].formatted_address;
      }
    });
  }

  uploadFile(event: any): any {
    const reader = new FileReader();
    const file = event.target!.files[0];
    if (event.target!.files && event.target.files[0]) {
      reader.readAsDataURL(file);

      // When file uploads push it to file list
      reader.onload = () => {
        const media: IMedia = {};
        media.fileName = file.name;
        media.fileReader = reader.result;
        media.fileType = file.name.substring(Number(file.name.lastIndexOf('.')) + 1);
        this.urlFilesList.push(media);
      };
    }
  }

  isImage(extension: string): boolean {
    const imageExtentions = ['JPEG', 'JPG', 'PNG', 'GIF', 'TIFF', 'JFIF']; // Array of image extention
    return imageExtentions.indexOf(extension.toLocaleUpperCase()) !== -1;
  }

  isVideo(extension: string): boolean {
    const imageExtentions = ['MP4', 'MOV', 'AVI']; // Array of videos extention
    return imageExtentions.indexOf(extension.toLocaleUpperCase()) !== -1;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const park = this.createFromForm();
    this.subscribeToSaveResponse(this.parkService.create(park));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPark>>): void {
    result.subscribe(
      resPark => {
        if (this.equipement.id) {
          this.equipement.park = resPark.body;
          this.equipementService.update(this.equipement).subscribe();
        }

        this.urlFilesList.forEach(media => {
          media.filePath = media.fileReader;
          media.park = resPark.body;
          media.authType = AuthType.LEARN;
          this.mediaService.create(media).subscribe();
        });
      },
      () => this.onSaveError(),
      () => this.onSaveSuccess()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected createFromForm(): IPark {
    return {
      ...new Park(),
      id: this.editForm.get(['id'])!.value,
      parkName: this.editForm.get(['parkName'])!.value,
      parkAddress: this.currentAdresse,
      longtitude: this.longitude,
      latitude: this.latitude,
      verified: false,
    };
  }
}
