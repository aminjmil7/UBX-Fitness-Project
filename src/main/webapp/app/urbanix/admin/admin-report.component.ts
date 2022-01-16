import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IReport, Report } from '../report-problem/report.model';
import { ReportService } from '../report-problem/report.service';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-admin-report',
  templateUrl: './admin-report.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminReportComponent implements OnInit {
  reports?: IReport[] = [];
  reportsSearch?: IReport[] = [];
  selectedReport: IReport = {};
  sizes: number[] = [5, 10, 20, 50];
  currentSearch!: string;
  size = 20;
  currentUser!: Account;
  password = '';

  constructor(private reportService: ReportService, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe((account: Account | null) => {
      this.currentUser = account!;
      this.loadAll();
    });
  }

  loadAll(): void {
    this.selectedReport = {};
    this.reportService.query({ size: this.size }).subscribe((res: HttpResponse<Report[]>) => {
      this.reports = res.body!;
      this.reports.forEach(report => {
        report.date = dayjs(new Date());
      });
      this.reportsSearch = res.body!;
    });
  }

  search(): void {
    if (this.currentSearch) {
      this.reports = this.reportsSearch!.filter(report =>
        report.message?.toLocaleLowerCase().match(this.currentSearch.toLocaleLowerCase())
      );
    } else {
      this.reports = this.reportsSearch;
    }
  }

  deleteReport(id: number): void {
    this.reportService.delete(id).subscribe(() => this.loadAll());
  }
}
