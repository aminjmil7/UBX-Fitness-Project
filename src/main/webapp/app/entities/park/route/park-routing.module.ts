import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParkComponent } from '../list/park.component';
import { ParkDetailComponent } from '../detail/park-detail.component';
import { ParkUpdateComponent } from '../update/park-update.component';
import { ParkRoutingResolveService } from './park-routing-resolve.service';

const parkRoute: Routes = [
  {
    path: '',
    component: ParkComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParkDetailComponent,
    resolve: {
      park: ParkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParkUpdateComponent,
    resolve: {
      park: ParkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParkUpdateComponent,
    resolve: {
      park: ParkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parkRoute)],
  exports: [RouterModule],
})
export class ParkRoutingModule {}
