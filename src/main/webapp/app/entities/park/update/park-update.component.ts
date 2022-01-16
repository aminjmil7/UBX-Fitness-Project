import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPark, Park } from '../park.model';
import { ParkService } from '../service/park.service';

@Component({
  selector: 'jhi-park-update',
  templateUrl: './park-update.component.html',
})
export class ParkUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    parkName: [],
    parkAddress: [],
    longtitude: [],
    latitude: [],
    verified: [],
    dateInstall: [],
    dateOpen: [],
    dateClose: [],
    note: [],
    reseller: [],
  });

  constructor(protected parkService: ParkService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ park }) => {
      if (park.id === undefined) {
        const today = dayjs().startOf('day');
        park.dateInstall = today;
        park.dateOpen = today;
        park.dateClose = today;
      }

      this.updateForm(park);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const park = this.createFromForm();
    if (park.id !== undefined) {
      this.subscribeToSaveResponse(this.parkService.update(park));
    } else {
      this.subscribeToSaveResponse(this.parkService.create(park));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPark>>): void {
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

  protected updateForm(park: IPark): void {
    this.editForm.patchValue({
      id: park.id,
      parkName: park.parkName,
      parkAddress: park.parkAddress,
      longtitude: park.longtitude,
      latitude: park.latitude,
      verified: park.verified,
      dateInstall: park.dateInstall ? park.dateInstall.format(DATE_TIME_FORMAT) : null,
      dateOpen: park.dateOpen ? park.dateOpen.format(DATE_TIME_FORMAT) : null,
      dateClose: park.dateClose ? park.dateClose.format(DATE_TIME_FORMAT) : null,
      note: park.note,
      reseller: park.reseller,
    });
  }

  protected createFromForm(): IPark {
    return {
      ...new Park(),
      id: this.editForm.get(['id'])!.value,
      parkName: this.editForm.get(['parkName'])!.value,
      parkAddress: this.editForm.get(['parkAddress'])!.value,
      longtitude: this.editForm.get(['longtitude'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      verified: this.editForm.get(['verified'])!.value,
      dateInstall: this.editForm.get(['dateInstall'])!.value
        ? dayjs(this.editForm.get(['dateInstall'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateOpen: this.editForm.get(['dateOpen'])!.value ? dayjs(this.editForm.get(['dateOpen'])!.value, DATE_TIME_FORMAT) : undefined,
      dateClose: this.editForm.get(['dateClose'])!.value ? dayjs(this.editForm.get(['dateClose'])!.value, DATE_TIME_FORMAT) : undefined,
      note: this.editForm.get(['note'])!.value,
      reseller: this.editForm.get(['reseller'])!.value,
    };
  }
}
