import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SharedModule } from 'app/shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportProblemComponent } from './report-problem.component';
import { ParkResolveService } from '../park/park-resolve.service';

const ReportProblemRoute: Routes = [
  {
    path: '',
    component: ReportProblemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id',
    component: ReportProblemComponent,
    resolve: {
      park: ParkResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(ReportProblemRoute),
    SharedModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    NgbModule,
  ],
  declarations: [ReportProblemComponent],
  exports: [RouterModule],
  entryComponents: [],
})
export class ReportProblemModule {}
