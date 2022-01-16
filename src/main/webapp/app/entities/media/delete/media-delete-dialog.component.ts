import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedia } from '../media.model';
import { MediaService } from '../service/media.service';

@Component({
  templateUrl: './media-delete-dialog.component.html',
})
export class MediaDeleteDialogComponent {
  media?: IMedia;

  constructor(protected mediaService: MediaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mediaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
