import { Component, Input, OnChanges } from '@angular/core';
import { IPark } from '../park.model';
@Component({
  selector: 'jhi-park-list',
  templateUrl: './park-list.component.html',
  styleUrls: ['./park-list-style.scss'],
})
export class ParkListComponent implements OnChanges {
  @Input() parks?: IPark[];

  ngOnChanges(): void {
    this.parks!.forEach(park => {
      if (park.distance! >= 1000) {
        park.distance = Number((park.distance! / 1000).toFixed(0));
        park.distanceUnit = 'km';
      } else {
        park.distance = Number(park.distance!.toFixed(0));
        park.distanceUnit = 'm';
      }
    });
    this.parks = this.parks!.sort((a, b) => (a.distance! < b.distance! ? -1 : 1));
  }
}
