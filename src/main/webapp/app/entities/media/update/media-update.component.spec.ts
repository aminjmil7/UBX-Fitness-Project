jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MediaService } from '../service/media.service';
import { IMedia, Media } from '../media.model';
import { IPark } from 'app/entities/park/park.model';
import { ParkService } from 'app/entities/park/service/park.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';

import { MediaUpdateComponent } from './media-update.component';

describe('Component Tests', () => {
  describe('Media Management Update Component', () => {
    let comp: MediaUpdateComponent;
    let fixture: ComponentFixture<MediaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mediaService: MediaService;
    let parkService: ParkService;
    let equipementService: EquipementService;
    let reportService: ReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MediaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MediaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MediaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mediaService = TestBed.inject(MediaService);
      parkService = TestBed.inject(ParkService);
      equipementService = TestBed.inject(EquipementService);
      reportService = TestBed.inject(ReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Park query and add missing value', () => {
        const media: IMedia = { id: 456 };
        const park: IPark = { id: 43672 };
        media.park = park;

        const parkCollection: IPark[] = [{ id: 13457 }];
        spyOn(parkService, 'query').and.returnValue(of(new HttpResponse({ body: parkCollection })));
        const additionalParks = [park];
        const expectedCollection: IPark[] = [...additionalParks, ...parkCollection];
        spyOn(parkService, 'addParkToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ media });
        comp.ngOnInit();

        expect(parkService.query).toHaveBeenCalled();
        expect(parkService.addParkToCollectionIfMissing).toHaveBeenCalledWith(parkCollection, ...additionalParks);
        expect(comp.parksSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Equipement query and add missing value', () => {
        const media: IMedia = { id: 456 };
        const equipement: IEquipement = { id: 68757 };
        media.equipement = equipement;

        const equipementCollection: IEquipement[] = [{ id: 42295 }];
        spyOn(equipementService, 'query').and.returnValue(of(new HttpResponse({ body: equipementCollection })));
        const additionalEquipements = [equipement];
        const expectedCollection: IEquipement[] = [...additionalEquipements, ...equipementCollection];
        spyOn(equipementService, 'addEquipementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ media });
        comp.ngOnInit();

        expect(equipementService.query).toHaveBeenCalled();
        expect(equipementService.addEquipementToCollectionIfMissing).toHaveBeenCalledWith(equipementCollection, ...additionalEquipements);
        expect(comp.equipementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Report query and add missing value', () => {
        const media: IMedia = { id: 456 };
        const report: IReport = { id: 25937 };
        media.report = report;

        const reportCollection: IReport[] = [{ id: 33722 }];
        spyOn(reportService, 'query').and.returnValue(of(new HttpResponse({ body: reportCollection })));
        const additionalReports = [report];
        const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
        spyOn(reportService, 'addReportToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ media });
        comp.ngOnInit();

        expect(reportService.query).toHaveBeenCalled();
        expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(reportCollection, ...additionalReports);
        expect(comp.reportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const media: IMedia = { id: 456 };
        const park: IPark = { id: 90556 };
        media.park = park;
        const equipement: IEquipement = { id: 75142 };
        media.equipement = equipement;
        const report: IReport = { id: 90665 };
        media.report = report;

        activatedRoute.data = of({ media });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(media));
        expect(comp.parksSharedCollection).toContain(park);
        expect(comp.equipementsSharedCollection).toContain(equipement);
        expect(comp.reportsSharedCollection).toContain(report);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const media = { id: 123 };
        spyOn(mediaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ media });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: media }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mediaService.update).toHaveBeenCalledWith(media);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const media = new Media();
        spyOn(mediaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ media });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: media }));
        saveSubject.complete();

        // THEN
        expect(mediaService.create).toHaveBeenCalledWith(media);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const media = { id: 123 };
        spyOn(mediaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ media });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mediaService.update).toHaveBeenCalledWith(media);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackParkById', () => {
        it('Should return tracked Park primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackParkById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEquipementById', () => {
        it('Should return tracked Equipement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEquipementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackReportById', () => {
        it('Should return tracked Report primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
