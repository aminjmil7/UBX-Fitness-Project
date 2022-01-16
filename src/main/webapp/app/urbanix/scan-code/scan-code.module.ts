import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SharedModule } from 'app/shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ScanCodeComponent } from './scan-code.component';
import { ZXingScannerModule } from '@zxing/ngx-scanner';

const ScanCodeRoute: Routes = [
  {
    path: '',
    component: ScanCodeComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(ScanCodeRoute),
    SharedModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    ZXingScannerModule,
    NgbModule,
  ],
  declarations: [ScanCodeComponent],
  exports: [RouterModule],
  entryComponents: [],
})
export class ScanCodeModule {}
