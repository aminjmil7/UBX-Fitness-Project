import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';

@Component({
  selector: 'jhi-admin-nav-bar',
  templateUrl: './admin-nav-bar.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminNavBarComponent implements OnInit {
  currentUser!: Account | null;

  constructor(protected accountService: AccountService, protected loginService: LoginService, protected router: Router) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe((account: Account | null) => {
      this.currentUser = account;
    });
  }

  logout(): void {
    this.loginService.logout();
    this.currentUser = null;
    this.router.navigate(['/login/Admin']);
    window.location.reload();
  }
}
