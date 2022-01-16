import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ParkComponent } from './list/park.component';
import { ParkDetailComponent } from './detail/park-detail.component';
import { ParkUpdateComponent } from './update/park-update.component';
import { ParkDeleteDialogComponent } from './delete/park-delete-dialog.component';
import { ParkRoutingModule } from './route/park-routing.module';

@NgModule({
  imports: [SharedModule, ParkRoutingModule],
  declarations: [ParkComponent, ParkDetailComponent, ParkUpdateComponent, ParkDeleteDialogComponent],
  entryComponents: [ParkDeleteDialogComponent],
})
export class ParkModule {}
