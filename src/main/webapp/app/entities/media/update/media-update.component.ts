import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedia, Media } from '../media.model';
import { MediaService } from '../service/media.service';
import { IPark } from 'app/entities/park/park.model';
import { ParkService } from 'app/entities/park/service/park.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';

@Component({
  selector: 'jhi-media-update',
  templateUrl: './media-update.component.html',
})
export class MediaUpdateComponent implements OnInit {
  isSaving = false;

  parksSharedCollection: IPark[] = [];
  equipementsSharedCollection: IEquipement[] = [];
  reportsSharedCollection: IReport[] = [];

  editForm = this.fb.group({
    id: [],
    fileName: [],
    filePath: [],
    fileType: [],
    authType: [],
    park: [],
    equipement: [],
    report: [],
  });

  constructor(
    protected mediaService: MediaService,
    protected parkService: ParkService,
    protected equipementService: EquipementService,
    protected reportService: ReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ media }) => {
      this.updateForm(media);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const media = this.createFromForm();
    if (media.id !== undefined) {
      this.subscribeToSaveResponse(this.mediaService.update(media));
    } else {
      this.subscribeToSaveResponse(this.mediaService.create(media));
    }
  }

  trackParkById(index: number, item: IPark): number {
    return item.id!;
  }

  trackEquipementById(index: number, item: IEquipement): number {
    return item.id!;
  }

  trackReportById(index: number, item: IReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedia>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
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

  protected updateForm(media: IMedia): void {
    this.editForm.patchValue({
      id: media.id,
      fileName: media.fileName,
      filePath: media.filePath,
      fileType: media.fileType,
      authType: media.authType,
      park: media.park,
      equipement: media.equipement,
      report: media.report,
    });

    this.parksSharedCollection = this.parkService.addParkToCollectionIfMissing(this.parksSharedCollection, media.park);
    this.equipementsSharedCollection = this.equipementService.addEquipementToCollectionIfMissing(
      this.equipementsSharedCollection,
      media.equipement
    );
    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing(this.reportsSharedCollection, media.report);
  }

  protected loadRelationshipsOptions(): void {
    this.parkService
      .query()
      .pipe(map((res: HttpResponse<IPark[]>) => res.body ?? []))
      .pipe(map((parks: IPark[]) => this.parkService.addParkToCollectionIfMissing(parks, this.editForm.get('park')!.value)))
      .subscribe((parks: IPark[]) => (this.parksSharedCollection = parks));

    this.equipementService
      .query()
      .pipe(map((res: HttpResponse<IEquipement[]>) => res.body ?? []))
      .pipe(
        map((equipements: IEquipement[]) =>
          this.equipementService.addEquipementToCollectionIfMissing(equipements, this.editForm.get('equipement')!.value)
        )
      )
      .subscribe((equipements: IEquipement[]) => (this.equipementsSharedCollection = equipements));

    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing(reports, this.editForm.get('report')!.value)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }

  protected createFromForm(): IMedia {
    return {
      ...new Media(),
      id: this.editForm.get(['id'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      filePath: this.editForm.get(['filePath'])!.value,
      fileType: this.editForm.get(['fileType'])!.value,
      authType: this.editForm.get(['authType'])!.value,
      park: this.editForm.get(['park'])!.value,
      equipement: this.editForm.get(['equipement'])!.value,
      report: this.editForm.get(['report'])!.value,
    };
  }
}
