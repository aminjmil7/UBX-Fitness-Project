import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPark } from '../park.model';

@Component({
  selector: 'jhi-park-detail',
  templateUrl: './park-detail.component.html',
})
export class ParkDetailComponent implements OnInit {
  park: IPark | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ park }) => {
      this.park = park;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
