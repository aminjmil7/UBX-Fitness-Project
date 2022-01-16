import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IEquipement } from '../equipement/equipement.model';
import { EquipementService } from '../equipement/equipement.service';
import { IMedia } from '../media/media.model';
import { AuthType } from '../media/auth-type.model';
import { MediaService } from '../media/media.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPark } from '../park/park.model';
import { ParkService } from '../park/park.service';
import { ReportService } from '../report-problem/report.service';
import { Report } from '../report-problem/report.model';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-admin-park',
  templateUrl: './admin-park.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminParkComponent implements OnInit {
  parks: IPark[] = [];
  parksSearch: IPark[] = [];
  selectedPark: IPark = {};
  sizes: number[] = [5, 10, 20, 50];
  currentSearch!: string;
  size = 20;
  currentUser!: Account;
  password = '';
  equipementID!: number;
  messageSuccess!: string;
  messageError!: string;
  dateInstall!: string;
  dateOpen!: string;
  dateClose!: string;

  constructor(
    protected parkService: ParkService,
    protected equipementService: EquipementService,
    protected reportService: ReportService,
    protected accountService: AccountService,
    protected mediaService: MediaService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe((account: Account | null) => {
      this.currentUser = account!;
      this.loadAll();
    });
  }

  loadAll(): void {
    this.selectedPark = {};
    this.parkService.query({ size: this.size }).subscribe((res: HttpResponse<IPark[]>) => {
      this.parks = res.body!;
      this.parksSearch = res.body!;
      this.parks.forEach(element => {
        element.media = [];
      });
    });
  }

  search(): void {
    if (this.currentSearch) {
      this.parks = this.parksSearch.filter(park => park.parkName?.toLocaleLowerCase().match(this.currentSearch.toLocaleLowerCase()));
    } else {
      this.parks = this.parksSearch;
    }
  }

  save(): void {
    this.selectedPark.verified = this.selectedPark.verified ?? false;
    this.selectedPark.dateInstall = this.dateInstall ? dayjs(this.dateInstall) : null;
    if (this.dateOpen) {
      const dateOpen = new Date();
      const hoursOpen = Number(this.dateOpen.toString().split(':')[0]);
      const minOpen = Number(this.dateOpen.toString().split(':')[1]);
      dateOpen.setHours(hoursOpen);
      dateOpen.setMinutes(minOpen);
      this.selectedPark.dateOpen = dayjs(dateOpen);
    }
    if (this.dateClose) {
      const dateClose = new Date();
      const hoursClose = Number(this.dateClose.toString().split(':')[0]);
      const minClose = Number(this.dateClose.toString().split(':')[1]);
      dateClose.setHours(hoursClose);
      dateClose.setMinutes(minClose);
      this.selectedPark.dateClose = dayjs(dateClose);
    }

    if (this.selectedPark.id) {
      this.parkService.update(this.selectedPark).subscribe();
    } else {
      this.parkService.create(this.selectedPark).subscribe(resEquip => {
        this.selectedPark.media!.forEach(media => {
          media.park = resEquip.body;
          media.authType = AuthType.LEARN;
          this.mediaService.create(media).subscribe();
        });
        this.loadAll();
      });
    }
  }

  loadPark(park: IPark): void {
    this.selectedPark = park;

    this.dateInstall = dayjs(this.selectedPark.dateInstall!).format('YYYY-MM-DD');
    this.dateOpen = dayjs(this.selectedPark.dateOpen!).format('HH:mm');
    this.dateClose = dayjs(this.selectedPark.dateClose!).format('HH:mm');

    this.loadParkMedia();
    this.loadParkEquipements();
    this.loadParkReports();
  }

  loadParkMedia(): void {
    this.selectedPark.media = [];
    this.mediaService
      .query({
        'parkId.equals': this.selectedPark.id,
        'authType.equals': 'LEARN',
        size: 100,
      })
      .subscribe((resMedia: HttpResponse<IMedia[]>) => {
        if (resMedia.body?.length) {
          this.selectedPark.media = resMedia.body;
        } else {
          this.selectedPark.media!.push({ filePath: '../../../../content/images/no-image.png' });
        }
      });
  }

  loadParkEquipements(): void {
    this.selectedPark.equipements = [];
    this.equipementService.query({ 'parkId.equals': this.selectedPark.id, size: 1000 }).subscribe((resEq: HttpResponse<IEquipement[]>) => {
      if (resEq.body?.length) {
        this.selectedPark.equipements = resEq.body;
        this.selectedPark.equipements.forEach(equipement => {
          equipement.modalPreview = '../../../../content/images/no-image.png';
          this.mediaService
            .query({
              'equipementId.equals': equipement.id,
              'authType.equals': 'LEARN',
              size: 100,
            })
            .subscribe((resMedia: HttpResponse<IMedia[]>) => {
              if (resMedia.body?.length) {
                equipement.modalPreview = resMedia.body[0].filePath;
              }
            });
        });
      }
    });
  }

  loadParkReports(): void {
    this.selectedPark.reports = [];
    this.reportService
      .query({
        'parkId.equals': this.selectedPark.id,
        size: 100,
      })
      .subscribe((res: HttpResponse<Report[]>) => (this.selectedPark.reports = res.body));
  }

  deletePark(id: number): void {
    this.parkService.delete(id).subscribe(() => this.loadAll());
  }

  uploadFile(event: any): any {
    const reader = new FileReader();
    const file = event.target!.files[0];
    if (event.target!.files && event.target.files[0]) {
      reader.readAsDataURL(file);

      // When file uploads push it to file list
      reader.onload = () => {
        if (this.selectedPark.id) {
          const media: IMedia = {};
          media.filePath = reader.result!.toString();
          media.park = this.selectedPark;
          media.authType = AuthType.LEARN;
          this.mediaService.create(media).subscribe(resMedia => {
            this.selectedPark.media?.push(resMedia.body!);
          });
        } else {
          this.selectedPark.media?.push({ filePath: reader.result!.toString() });
        }
      };
    }
  }

  newPark(): void {
    this.selectedPark = {};
    this.selectedPark.media = [];
  }

  deleteMedia(media: IMedia): void {
    const index: number = this.selectedPark.media!.indexOf(media);
    if (index !== -1) {
      this.selectedPark.media!.splice(index, 1);
    }
    if (media.id) {
      this.mediaService.delete(media.id).subscribe();
    }
  }

  openMedia(contentMedia: any): void {
    this.modalService.open(contentMedia, { size: 'md' });
  }

  closeMedia(): void {
    this.modalService.dismissAll();
  }

  AddEquipement(): void {
    this.messageSuccess = '';
    this.messageError = '';
    if (this.equipementID) {
      this.equipementService.query({ 'id.equals': this.equipementID, size: 1 }).subscribe((resEq: HttpResponse<IEquipement[]>) => {
        if (resEq.body?.length) {
          resEq.body[0].park = this.selectedPark;
          this.equipementService.update(resEq.body[0]).subscribe(
            () => {
              this.messageSuccess = 'Added Equipment Successfully';
              this.loadParkEquipements();
            },
            () => (this.messageError = 'Equipment Not Added')
          );
        } else {
          this.messageError = 'Equipment ID Not Exist';
        }
      });
    } else {
      this.messageError = 'Equipment ID Not valid';
    }
  }

  deleteEquipement(equipement: IEquipement): void {
    this.mediaService
      .query({
        'equipementId.equals': equipement.id,
        size: 100,
      })
      .subscribe((resMedia: HttpResponse<IMedia[]>) => {
        let i = 0;
        if (resMedia.body?.length) {
          resMedia.body.forEach(media => {
            this.mediaService.delete(media.id!).subscribe(() => i++);
          });
        }
        if (resMedia.body?.length === i) {
          this.equipementService.delete(equipement.id!).subscribe(() => {
            const index: number = this.selectedPark.equipements!.indexOf(equipement);
            if (index !== -1) {
              this.selectedPark.equipements!.splice(index, 1);
            }
          });
        }
      });
  }
}
