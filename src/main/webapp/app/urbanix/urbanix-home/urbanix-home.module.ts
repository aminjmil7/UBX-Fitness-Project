import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SharedModule } from 'app/shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UrbanixHomeComponent } from './urbanix-home.component';

const UrbanixHomeRoute: Routes = [
  {
    path: '',
    component: UrbanixHomeComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(UrbanixHomeRoute),
    SharedModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    NgbModule,
  ],
  declarations: [UrbanixHomeComponent],
  exports: [RouterModule],
  entryComponents: [],
})
export class UrbanixHomeModule {}
