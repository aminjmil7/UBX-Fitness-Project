import { Component } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMedia } from '../../media/media.model';
import { MediaService } from '../../media/media.service';
import { IEquipement, Equipement } from '../../equipement/equipement.model';
import { EquipementService } from '../../equipement/equipement.service';
import { AuthType } from 'app/urbanix/media/auth-type.model';

@Component({
  selector: 'jhi-register-equipement',
  templateUrl: './register-equipement.component.html',
  styleUrls: ['./register-equipement.component.scss'],
})
export class RegisterEquipementComponent {
  isSaving = false;

  urlFilesList: IMedia[] = [];

  editForm = this.fb.group({
    id: [],
    modelName: [],
    modelNumber: [],
    instruction: [],
    verified: [],
    park: [],
  });

  constructor(
    protected equipementService: EquipementService,
    protected mediaService: MediaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

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
    const equipement = this.createFromForm();
    this.subscribeToSaveResponse(this.equipementService.create(equipement));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipement>>): void {
    result.subscribe(
      resEquipement => {
        this.urlFilesList.forEach(media => {
          media.filePath = media.fileReader;
          media.equipement = resEquipement.body;
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

  protected createFromForm(): IEquipement {
    return {
      ...new Equipement(),
      id: this.editForm.get(['id'])!.value,
      modelName: this.editForm.get(['modelName'])!.value,
      modelNumber: this.editForm.get(['modelNumber'])!.value,
      instruction: this.editForm.get(['instruction'])!.value,
      verified: this.editForm.get(['verified'])!.value,
      park: this.editForm.get(['park'])!.value,
    };
  }
}
