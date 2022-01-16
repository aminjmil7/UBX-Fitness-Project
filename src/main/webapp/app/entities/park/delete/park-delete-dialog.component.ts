import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPark } from '../park.model';
import { ParkService } from '../service/park.service';

@Component({
  templateUrl: './park-delete-dialog.component.html',
})
export class ParkDeleteDialogComponent {
  park?: IPark;

  constructor(protected parkService: ParkService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
