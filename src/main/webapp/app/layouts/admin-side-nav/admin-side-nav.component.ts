import { Component } from '@angular/core';

@Component({
  selector: 'jhi-admin-side-nav',
  templateUrl: './admin-side-nav.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminSideNavComponent {
  selectedRoute = '';
  constructor() {
    const n = window.location.href.lastIndexOf('/');
    this.selectedRoute = window.location.href
      .toLocaleUpperCase()
      .substring(n + 1)
      .toLowerCase();
  }
}
