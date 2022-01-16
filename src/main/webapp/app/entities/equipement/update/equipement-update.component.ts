import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEquipement, Equipement } from '../equipement.model';
import { EquipementService } from '../service/equipement.service';
import { IPark } from 'app/entities/park/park.model';
import { ParkService } from 'app/entities/park/service/park.service';

@Component({
  selector: 'jhi-equipement-update',
  templateUrl: './equipement-update.component.html',
})
export class EquipementUpdateComponent implements OnInit {
  isSaving = false;

  parksSharedCollection: IPark[] = [];

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
    protected parkService: ParkService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipement }) => {
      this.updateForm(equipement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const equipement = this.createFromForm();
    if (equipement.id !== undefined) {
      this.subscribeToSaveResponse(this.equipementService.update(equipement));
    } else {
      this.subscribeToSaveResponse(this.equipementService.create(equipement));
    }
  }

  trackParkById(index: number, item: IPark): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipement>>): void {
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

  protected updateForm(equipement: IEquipement): void {
    this.editForm.patchValue({
      id: equipement.id,
      modelName: equipement.modelName,
      modelNumber: equipement.modelNumber,
      instruction: equipement.instruction,
      verified: equipement.verified,
      park: equipement.park,
    });

    this.parksSharedCollection = this.parkService.addParkToCollectionIfMissing(this.parksSharedCollection, equipement.park);
  }

  protected loadRelationshipsOptions(): void {
    this.parkService
      .query()
      .pipe(map((res: HttpResponse<IPark[]>) => res.body ?? []))
      .pipe(map((parks: IPark[]) => this.parkService.addParkToCollectionIfMissing(parks, this.editForm.get('park')!.value)))
      .subscribe((parks: IPark[]) => (this.parksSharedCollection = parks));
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
