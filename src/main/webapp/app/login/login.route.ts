import { Routes } from '@angular/router';
import { LoginAdminComponent } from './login-admin.component';
import { LoginComponent } from './login.component';

export const LOGIN_ROUTE: Routes = [
  {
    path: '',
    component: LoginComponent,
    data: {
      pageTitle: 'login.title',
    },
  },
  {
    path: 'Admin',
    component: LoginAdminComponent,
    data: {
      pageTitle: 'login.title',
    },
  },
];
