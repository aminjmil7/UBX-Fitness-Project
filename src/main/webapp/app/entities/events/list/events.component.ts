import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvents } from '../events.model';
import { EventsService } from '../service/events.service';
import { EventsDeleteDialogComponent } from '../delete/events-delete-dialog.component';

@Component({
  selector: 'jhi-events',
  templateUrl: './events.component.html',
})
export class EventsComponent implements OnInit {
  events?: IEvents[];
  isLoading = false;

  constructor(protected eventsService: EventsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eventsService.query().subscribe(
      (res: HttpResponse<IEvents[]>) => {
        this.isLoading = false;
        this.events = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEvents): number {
    return item.id!;
  }

  delete(events: IEvents): void {
    const modalRef = this.modalService.open(EventsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.events = events;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
