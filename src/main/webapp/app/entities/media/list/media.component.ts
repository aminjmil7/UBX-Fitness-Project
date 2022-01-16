import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedia } from '../media.model';
import { MediaService } from '../service/media.service';
import { MediaDeleteDialogComponent } from '../delete/media-delete-dialog.component';

@Component({
  selector: 'jhi-media',
  templateUrl: './media.component.html',
})
export class MediaComponent implements OnInit {
  media?: IMedia[];
  isLoading = false;

  constructor(protected mediaService: MediaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.mediaService.query().subscribe(
      (res: HttpResponse<IMedia[]>) => {
        this.isLoading = false;
        this.media = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMedia): number {
    return item.id!;
  }

  delete(media: IMedia): void {
    const modalRef = this.modalService.open(MediaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.media = media;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
