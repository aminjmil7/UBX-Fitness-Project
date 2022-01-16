import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SharedModule } from 'app/shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkoutCalanderComponent } from './workout-calander.component';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';

const WorkoutCalanderRoute: Routes = [
  {
    path: '',
    component: WorkoutCalanderComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(WorkoutCalanderRoute),
    SharedModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatCardModule,
    MatDatepickerModule,
    NgbModule,
  ],
  declarations: [WorkoutCalanderComponent],
  exports: [RouterModule],
  entryComponents: [],
})
export class WorkoutCalanderModule {}
