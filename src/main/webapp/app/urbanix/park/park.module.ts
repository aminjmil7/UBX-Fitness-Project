import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SharedModule } from 'app/shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegisterParkComponent } from './register-park/register-park.component';
import { ParkListComponent } from './park-list/park-list.component';
import { ParkNearMeComponent } from './park-near-me/park-near-me.component';
import { MatTabsModule } from '@angular/material/tabs';
import { AgmCoreModule } from '@agm/core';
import { ParkViewComponent } from './park-view/park-view.component';
import { ParkResolveService } from './park-resolve.service';
import { EquipementResolveService } from '../equipement/equipement-resolve.service';

const ParkRoute: Routes = [
  {
    path: 'register',
    component: RegisterParkComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'register/:id',
    component: RegisterParkComponent,
    resolve: {
      equipement: EquipementResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'list',
    component: ParkListComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'near-me',
    component: ParkNearMeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/park-view',
    component: ParkViewComponent,
    resolve: {
      park: ParkResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyAjiDifO5ZFHdPFRJG263skV1wOTNoVUgI',
    }),
    RouterModule.forChild(ParkRoute),
    SharedModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatTabsModule,
    NgbModule,
  ],
  declarations: [RegisterParkComponent, ParkNearMeComponent, ParkListComponent, ParkViewComponent],
  exports: [RouterModule],
  entryComponents: [],
})
export class ParkModule {}
