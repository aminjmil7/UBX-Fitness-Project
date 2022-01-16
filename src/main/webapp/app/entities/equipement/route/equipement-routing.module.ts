import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EquipementComponent } from '../list/equipement.component';
import { EquipementDetailComponent } from '../detail/equipement-detail.component';
import { EquipementUpdateComponent } from '../update/equipement-update.component';
import { EquipementRoutingResolveService } from './equipement-routing-resolve.service';

const equipementRoute: Routes = [
  {
    path: '',
    component: EquipementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EquipementDetailComponent,
    resolve: {
      equipement: EquipementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EquipementUpdateComponent,
    resolve: {
      equipement: EquipementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EquipementUpdateComponent,
    resolve: {
      equipement: EquipementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(equipementRoute)],
  exports: [RouterModule],
})
export class EquipementRoutingModule {}
