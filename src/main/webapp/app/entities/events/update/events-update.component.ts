import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEvents, Events } from '../events.model';
import { EventsService } from '../service/events.service';

@Component({
  selector: 'jhi-events-update',
  templateUrl: './events-update.component.html',
})
export class EventsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    eventName: [],
    eventDate: [],
    user_id: [],
  });

  constructor(protected eventsService: EventsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ events }) => {
      if (events.id === undefined) {
        const today = dayjs().startOf('day');
        events.eventDate = today;
      }

      this.updateForm(events);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const events = this.createFromForm();
    if (events.id !== undefined) {
      this.subscribeToSaveResponse(this.eventsService.update(events));
    } else {
      this.subscribeToSaveResponse(this.eventsService.create(events));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvents>>): void {
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

  protected updateForm(events: IEvents): void {
    this.editForm.patchValue({
      id: events.id,
      eventName: events.eventName,
      eventDate: events.eventDate ? events.eventDate.format(DATE_TIME_FORMAT) : null,
      user_id: events.user_id,
    });
  }

  protected createFromForm(): IEvents {
    return {
      ...new Events(),
      id: this.editForm.get(['id'])!.value,
      eventName: this.editForm.get(['eventName'])!.value,
      eventDate: this.editForm.get(['eventDate'])!.value ? dayjs(this.editForm.get(['eventDate'])!.value, DATE_TIME_FORMAT) : undefined,
      user_id: this.editForm.get(['user_id'])!.value,
    };
  }
}
