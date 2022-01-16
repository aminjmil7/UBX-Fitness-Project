import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { User } from 'app/admin/user-management/user-management.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { TranslateService } from '@ngx-translate/core';
import { RegisterService } from 'app/account/register/register.service';

@Component({
  selector: 'jhi-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminUserComponent implements OnInit {
  users: User[] = [];
  usersSearch: User[] = [];
  sizes: number[] = [5, 10, 20, 50];
  currentSearch!: string;
  size = 20;
  selectedUser: User = {};
  currentUser!: Account;
  totalUsers = 0;

  constructor(
    private userService: UserManagementService,
    protected accountService: AccountService,
    private translateService: TranslateService,
    private registerService: RegisterService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe((account: Account | null) => {
      this.currentUser = account!;
      this.userService.query({ size: 200000 }).subscribe((res: HttpResponse<User[]>) => (this.totalUsers = res.body!.length));
      this.loadAll();
    });
  }

  loadAll(): void {
    this.selectedUser = {};
    this.userService.query({ size: this.size }).subscribe((res: HttpResponse<User[]>) => {
      this.users = res.body!;
      this.usersSearch = res.body!;
    });
  }

  search(): void {
    if (this.currentSearch) {
      this.users = this.usersSearch.filter(user => user.firstName?.toLocaleLowerCase().match(this.currentSearch.toLocaleLowerCase()));
    } else {
      this.users = this.usersSearch;
    }
  }

  save(): void {
    this.selectedUser.activated = this.selectedUser.activated ?? false;
    this.selectedUser.login = this.selectedUser.firstName!;
    if (this.selectedUser.id) {
      this.selectedUser.lastModifiedBy = this.currentUser.login;
      this.selectedUser.lastModifiedDate = new Date();
      this.userService.update(this.selectedUser).subscribe();
    }
  }

  deleteUser(login: string): void {
    this.userService.delete(login).subscribe(() => this.loadAll());
  }

  uploadFile(event: any): any {
    const reader = new FileReader();
    const file = event.target!.files[0];
    if (event.target!.files && event.target.files[0]) {
      reader.readAsDataURL(file);

      // When file uploads push it to file list
      reader.onload = () => {
        this.selectedUser.imageUrl = reader.result!.toString();
      };
    }
  }
}
