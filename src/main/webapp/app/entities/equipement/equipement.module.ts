import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EquipementComponent } from './list/equipement.component';
import { EquipementDetailComponent } from './detail/equipement-detail.component';
import { EquipementUpdateComponent } from './update/equipement-update.component';
import { EquipementDeleteDialogComponent } from './delete/equipement-delete-dialog.component';
import { EquipementRoutingModule } from './route/equipement-routing.module';

@NgModule({
  imports: [SharedModule, EquipementRoutingModule],
  declarations: [EquipementComponent, EquipementDetailComponent, EquipementUpdateComponent, EquipementDeleteDialogComponent],
  entryComponents: [EquipementDeleteDialogComponent],
})
export class EquipementModule {}
