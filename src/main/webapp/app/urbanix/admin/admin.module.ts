import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SharedModule } from 'app/shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdminComponent } from './admin.component';
import { AdminUserComponent } from './admin-user.component';
import { AdminEquipementComponent } from './admin-equipement.component';
import { AdminParkComponent } from './admin-park.component';
import { AdminReportComponent } from './admin-report.component';
import { MatBadgeModule } from '@angular/material/badge';

const AdminRoute: Routes = [
  {
    path: '',
    component: AdminComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user',
    component: AdminUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'equipement',
    component: AdminEquipementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'park',
    component: AdminParkComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'report',
    component: AdminReportComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(AdminRoute),
    SharedModule,
    MatSidenavModule,
    MatBadgeModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    NgbModule,
  ],
  declarations: [AdminComponent, AdminUserComponent, AdminEquipementComponent, AdminParkComponent, AdminReportComponent],
  exports: [RouterModule],
  entryComponents: [],
})
export class AdminModule {}
