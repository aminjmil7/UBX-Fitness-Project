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

@Component({
  selector: 'jhi-admin-equipement',
  templateUrl: './admin-equipement.component.html',
  styleUrls: ['./style.scss'],
})
export class AdminEquipementComponent implements OnInit {
  equipements: IEquipement[] = [];
  equipementsSearch: IEquipement[] = [];
  selectedEquipement: IEquipement = {};
  sizes: number[] = [5, 10, 20, 50];
  currentSearch!: string;
  size = 20;
  currentUser!: Account;
  password = '';

  constructor(
    protected equipementService: EquipementService,
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
    this.selectedEquipement = {};
    this.equipementService.query({ size: this.size }).subscribe((res: HttpResponse<IEquipement[]>) => {
      this.equipements = res.body!;
      this.equipementsSearch = res.body!;
      this.equipements.forEach(element => {
        element.media = [];
      });
    });
  }

  search(): void {
    if (this.currentSearch) {
      this.equipements = this.equipementsSearch.filter(equipement =>
        equipement.modelName?.toLocaleLowerCase().match(this.currentSearch.toLocaleLowerCase())
      );
    } else {
      this.equipements = this.equipementsSearch;
    }
  }

  save(): void {
    this.selectedEquipement.verified = this.selectedEquipement.verified ?? false;
    if (this.selectedEquipement.id) {
      this.equipementService.update(this.selectedEquipement).subscribe();
    } else {
      this.equipementService.create(this.selectedEquipement).subscribe(resEquip => {
        this.selectedEquipement.media!.forEach(media => {
          media.equipement = resEquip.body;
          media.authType = AuthType.LEARN;
          this.mediaService.create(media).subscribe();
        });
        this.loadAll();
      });
    }
  }

  loadEquipement(equipement: IEquipement): void {
    this.selectedEquipement = equipement;
    this.selectedEquipement.media = [];
    this.mediaService
      .query({
        'equipementId.equals': this.selectedEquipement.id,
        'authType.equals': 'LEARN',
        size: 1000,
      })
      .subscribe((resMedia: HttpResponse<IMedia[]>) => {
        if (resMedia.body?.length) {
          this.selectedEquipement.media = resMedia.body;
        } else {
          this.selectedEquipement.media!.push({ filePath: '../../../../content/images/no-image.png' });
        }
      });
  }

  deleteEquipement(id: number): void {
    this.equipementService.delete(id).subscribe(() => this.loadAll());
  }

  uploadFile(event: any): any {
    const reader = new FileReader();
    const file = event.target!.files[0];
    if (event.target!.files && event.target.files[0]) {
      reader.readAsDataURL(file);

      // When file uploads push it to file list
      reader.onload = () => {
        if (this.selectedEquipement.id) {
          const media: IMedia = {};
          media.filePath = reader.result!.toString();
          media.equipement = this.selectedEquipement;
          media.authType = AuthType.LEARN;
          this.mediaService.create(media).subscribe(resMedia => {
            this.selectedEquipement.media?.push(resMedia.body!);
          });
        } else {
          this.selectedEquipement.media?.push({ filePath: reader.result!.toString() });
        }
      };
    }
  }

  newEquipement(): void {
    this.selectedEquipement = {};
    this.selectedEquipement.media = [];
  }

  deleteMedia(media: IMedia): void {
    const index: number = this.selectedEquipement.media!.indexOf(media);
    if (index !== -1) {
      this.selectedEquipement.media!.splice(index, 1);
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
}
