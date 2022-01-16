import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPark } from '../park.model';
import { ParkService } from '../service/park.service';
import { ParkDeleteDialogComponent } from '../delete/park-delete-dialog.component';

@Component({
  selector: 'jhi-park',
  templateUrl: './park.component.html',
})
export class ParkComponent implements OnInit {
  parks?: IPark[];
  isLoading = false;

  constructor(protected parkService: ParkService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.parkService.query().subscribe(
      (res: HttpResponse<IPark[]>) => {
        this.isLoading = false;
        this.parks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPark): number {
    return item.id!;
  }

  delete(park: IPark): void {
    const modalRef = this.modalService.open(ParkDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.park = park;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
