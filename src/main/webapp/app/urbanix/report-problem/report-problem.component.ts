import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEquipement } from '../equipement/equipement.model';
import { EquipementService } from '../equipement//equipement.service';
import { IPark, Park } from '../park/park.model';
import { ParkService } from '../park/park.service';
import { IMedia } from '../media/media.model';
import { MediaService } from '../media/media.service';
import { ReportService } from './report.service';
import { IReport, Report } from './report.model';
import { AuthType } from '../media/auth-type.model';

@Component({
  selector: 'jhi-report-problem',
  templateUrl: './report-problem.component.html',
  styleUrls: ['./report-problem.component.scss'],
})
export class ReportProblemComponent implements OnInit {
  isSaving = false;

  urlFilesList: IMedia[] = [];

  equipementsSharedCollection: IEquipement[] = [];
  parksSharedCollection: IPark[] = [];

  editForm = this.fb.group({
    id: [],
    mail: [],
    message: [],
    equipement: [],
    park: [],
  });
  park: Park = {};

  constructor(
    protected reportService: ReportService,
    protected mediaService: MediaService,
    protected equipementService: EquipementService,
    protected parkService: ParkService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ park }) => {
      this.park = park || {};

      this.updateForm();
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
    const report = this.createFromForm();
    if (this.park.id) {
      report.park = this.park;
    }
    this.subscribeToSaveResponse(this.reportService.create(report));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      resReport => {
        this.urlFilesList.forEach(media => {
          media.filePath = media.fileReader;
          media.report = resReport.body;
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

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(): void {
    this.editForm.patchValue({
      id: [],
      mail: [],
      message: [],
      equipement: [],
      park: [],
    });
  }

  protected createFromForm(): IReport {
    return {
      ...new Report(),
      id: this.editForm.get(['id'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      message: this.editForm.get(['message'])!.value,
      equipement: this.editForm.get(['equipement'])!.value,
      park: this.editForm.get(['park'])!.value,
    };
  }
}
