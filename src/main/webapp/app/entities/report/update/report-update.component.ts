import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReport, Report } from '../report.model';
import { ReportService } from '../service/report.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { IPark } from 'app/entities/park/park.model';
import { ParkService } from 'app/entities/park/service/park.service';

@Component({
  selector: 'jhi-report-update',
  templateUrl: './report-update.component.html',
})
export class ReportUpdateComponent implements OnInit {
  isSaving = false;

  equipementsSharedCollection: IEquipement[] = [];
  parksSharedCollection: IPark[] = [];

  editForm = this.fb.group({
    id: [],
    mail: [],
    message: [],
    date: [],
    equipement: [],
    park: [],
  });

  constructor(
    protected reportService: ReportService,
    protected equipementService: EquipementService,
    protected parkService: ParkService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ report }) => {
      if (report.id === undefined) {
        const today = dayjs().startOf('day');
        report.date = today;
      }

      this.updateForm(report);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const report = this.createFromForm();
    if (report.id !== undefined) {
      this.subscribeToSaveResponse(this.reportService.update(report));
    } else {
      this.subscribeToSaveResponse(this.reportService.create(report));
    }
  }

  trackEquipementById(index: number, item: IEquipement): number {
    return item.id!;
  }

  trackParkById(index: number, item: IPark): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
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

  protected updateForm(report: IReport): void {
    this.editForm.patchValue({
      id: report.id,
      mail: report.mail,
      message: report.message,
      date: report.date ? report.date.format(DATE_TIME_FORMAT) : null,
      equipement: report.equipement,
      park: report.park,
    });

    this.equipementsSharedCollection = this.equipementService.addEquipementToCollectionIfMissing(
      this.equipementsSharedCollection,
      report.equipement
    );
    this.parksSharedCollection = this.parkService.addParkToCollectionIfMissing(this.parksSharedCollection, report.park);
  }

  protected loadRelationshipsOptions(): void {
    this.equipementService
      .query()
      .pipe(map((res: HttpResponse<IEquipement[]>) => res.body ?? []))
      .pipe(
        map((equipements: IEquipement[]) =>
          this.equipementService.addEquipementToCollectionIfMissing(equipements, this.editForm.get('equipement')!.value)
        )
      )
      .subscribe((equipements: IEquipement[]) => (this.equipementsSharedCollection = equipements));

    this.parkService
      .query()
      .pipe(map((res: HttpResponse<IPark[]>) => res.body ?? []))
      .pipe(map((parks: IPark[]) => this.parkService.addParkToCollectionIfMissing(parks, this.editForm.get('park')!.value)))
      .subscribe((parks: IPark[]) => (this.parksSharedCollection = parks));
  }

  protected createFromForm(): IReport {
    return {
      ...new Report(),
      id: this.editForm.get(['id'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      message: this.editForm.get(['message'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      equipement: this.editForm.get(['equipement'])!.value,
      park: this.editForm.get(['park'])!.value,
    };
  }
}
