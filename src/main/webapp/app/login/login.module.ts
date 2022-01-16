import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { LOGIN_ROUTE } from './login.route';
import { LoginComponent } from './login.component';
import { LoginAdminComponent } from './login-admin.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([...LOGIN_ROUTE])],
  declarations: [LoginComponent, LoginAdminComponent],
})
export class LoginModule {}
