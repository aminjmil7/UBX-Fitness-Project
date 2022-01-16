import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEquipement } from '../equipement.model';
import { EquipementService } from '../service/equipement.service';
import { EquipementDeleteDialogComponent } from '../delete/equipement-delete-dialog.component';

@Component({
  selector: 'jhi-equipement',
  templateUrl: './equipement.component.html',
})
export class EquipementComponent implements OnInit {
  equipements?: IEquipement[];
  isLoading = false;

  constructor(protected equipementService: EquipementService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.equipementService.query().subscribe(
      (res: HttpResponse<IEquipement[]>) => {
        this.isLoading = false;
        this.equipements = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEquipement): number {
    return item.id!;
  }

  delete(equipement: IEquipement): void {
    const modalRef = this.modalService.open(EquipementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.equipement = equipement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
