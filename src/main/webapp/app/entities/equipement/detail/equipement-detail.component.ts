import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipement } from '../equipement.model';

@Component({
  selector: 'jhi-equipement-detail',
  templateUrl: './equipement-detail.component.html',
})
export class EquipementDetailComponent implements OnInit {
  equipement: IEquipement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipement }) => {
      this.equipement = equipement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
