import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IEquipement } from '../../equipement/equipement.model';
import { EquipementService } from '../../equipement/equipement.service';
import { MediaService } from '../../media/media.service';
import { IMedia } from '../../media/media.model';
@Component({
  selector: 'jhi-equipement-list-service',
  templateUrl: './equipement-list-service.component.html',
  styleUrls: ['./equipement-style.scss'],
})
export class EquipementListServiceComponent implements OnInit {
  equipements?: IEquipement[] = [];
  OriginalEquipements: IEquipement[] = [];

  constructor(protected equipementService: EquipementService, protected mediaService: MediaService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.equipements = [];
    this.OriginalEquipements = [];
    this.equipementService.query({ 'verified.equals': true }).subscribe((resEq: HttpResponse<IEquipement[]>) => {
      this.equipements = resEq.body!;
      this.OriginalEquipements = resEq.body!;
      this.equipements.forEach(equipement => {
        equipement.modalPreview = '../../../../content/images/no-image.png';
        this.mediaService
          .query({ 'equipementId.equals': equipement.id, 'authType.equals': 'LEARN' })
          .subscribe((resMedia: HttpResponse<IMedia[]>) => {
            if (resMedia.body?.length) {
              equipement.modalPreview = resMedia.body[0].filePath ? resMedia.body[0].filePath : '../../../../content/images/no-image.png';
            }
          });
      });
    });
  }

  searchEquipement(search: string): void {
    if (search) {
      this.equipements = this.OriginalEquipements.filter(
        res =>
          res.modelName?.toLocaleLowerCase().match(search.toLocaleLowerCase()) ??
          res.modelNumber?.toLocaleLowerCase().match(search.toLocaleLowerCase())
      );
    } else {
      this.equipements = this.OriginalEquipements;
    }
  }
}
