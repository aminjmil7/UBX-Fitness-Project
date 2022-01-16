jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReportService } from '../service/report.service';
import { IReport, Report } from '../report.model';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { IPark } from 'app/entities/park/park.model';
import { ParkService } from 'app/entities/park/service/park.service';

import { ReportUpdateComponent } from './report-update.component';

describe('Component Tests', () => {
  describe('Report Management Update Component', () => {
    let comp: ReportUpdateComponent;
    let fixture: ComponentFixture<ReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reportService: ReportService;
    let equipementService: EquipementService;
    let parkService: ParkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reportService = TestBed.inject(ReportService);
      equipementService = TestBed.inject(EquipementService);
      parkService = TestBed.inject(ParkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Equipement query and add missing value', () => {
        const report: IReport = { id: 456 };
        const equipement: IEquipement = { id: 84202 };
        report.equipement = equipement;

        const equipementCollection: IEquipement[] = [{ id: 41568 }];
        spyOn(equipementService, 'query').and.returnValue(of(new HttpResponse({ body: equipementCollection })));
        const additionalEquipements = [equipement];
        const expectedCollection: IEquipement[] = [...additionalEquipements, ...equipementCollection];
        spyOn(equipementService, 'addEquipementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ report });
        comp.ngOnInit();

        expect(equipementService.query).toHaveBeenCalled();
        expect(equipementService.addEquipementToCollectionIfMissing).toHaveBeenCalledWith(equipementCollection, ...additionalEquipements);
        expect(comp.equipementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Park query and add missing value', () => {
        const report: IReport = { id: 456 };
        const park: IPark = { id: 97641 };
        report.park = park;

        const parkCollection: IPark[] = [{ id: 95257 }];
        spyOn(parkService, 'query').and.returnValue(of(new HttpResponse({ body: parkCollection })));
        const additionalParks = [park];
        const expectedCollection: IPark[] = [...additionalParks, ...parkCollection];
        spyOn(parkService, 'addParkToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ report });
        comp.ngOnInit();

        expect(parkService.query).toHaveBeenCalled();
        expect(parkService.addParkToCollectionIfMissing).toHaveBeenCalledWith(parkCollection, ...additionalParks);
        expect(comp.parksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const report: IReport = { id: 456 };
        const equipement: IEquipement = { id: 751 };
        report.equipement = equipement;
        const park: IPark = { id: 25007 };
        report.park = park;

        activatedRoute.data = of({ report });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(report));
        expect(comp.equipementsSharedCollection).toContain(equipement);
        expect(comp.parksSharedCollection).toContain(park);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const report = { id: 123 };
        spyOn(reportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ report });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: report }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reportService.update).toHaveBeenCalledWith(report);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const report = new Report();
        spyOn(reportService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ report });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: report }));
        saveSubject.complete();

        // THEN
        expect(reportService.create).toHaveBeenCalledWith(report);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const report = { id: 123 };
        spyOn(reportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ report });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reportService.update).toHaveBeenCalledWith(report);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEquipementById', () => {
        it('Should return tracked Equipement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEquipementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackParkById', () => {
        it('Should return tracked Park primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackParkById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
