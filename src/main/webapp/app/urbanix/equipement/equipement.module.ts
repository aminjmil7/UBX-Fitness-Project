import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EquipementListComponent } from './equipement-list/equipement-list.component';
import { EquipementListServiceComponent } from './equipement-list-service/equipement-list-service.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { RegisterEquipementComponent } from './register-equipement/register-equipement.component';
import { EquipementViewComponent } from './equipement-view/equipement-view.component';
import { EquipementResolveService } from './equipement-resolve.service';
import { EquipementServiceComponent } from './equipement-service/equipement-service.component';

const EquipementRoute: Routes = [
  {
    path: 'register',
    component: RegisterEquipementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'learn',
    component: EquipementListComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'service',
    component: EquipementListServiceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EquipementViewComponent,
    resolve: {
      equipement: EquipementResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/service',
    component: EquipementServiceComponent,
    resolve: {
      equipement: EquipementResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(EquipementRoute), CommonModule, SharedModule, MatFormFieldModule, MatIconModule],
  declarations: [
    RegisterEquipementComponent,
    EquipementListComponent,
    EquipementListServiceComponent,
    EquipementViewComponent,
    EquipementServiceComponent,
  ],
  exports: [RouterModule],
  entryComponents: [],
})
export class EquipementModule {}
