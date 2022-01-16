jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EquipementService } from '../service/equipement.service';
import { IEquipement, Equipement } from '../equipement.model';
import { IPark } from 'app/entities/park/park.model';
import { ParkService } from 'app/entities/park/service/park.service';

import { EquipementUpdateComponent } from './equipement-update.component';

describe('Component Tests', () => {
  describe('Equipement Management Update Component', () => {
    let comp: EquipementUpdateComponent;
    let fixture: ComponentFixture<EquipementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let equipementService: EquipementService;
    let parkService: ParkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EquipementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EquipementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EquipementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      equipementService = TestBed.inject(EquipementService);
      parkService = TestBed.inject(ParkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Park query and add missing value', () => {
        const equipement: IEquipement = { id: 456 };
        const park: IPark = { id: 26134 };
        equipement.park = park;

        const parkCollection: IPark[] = [{ id: 82592 }];
        spyOn(parkService, 'query').and.returnValue(of(new HttpResponse({ body: parkCollection })));
        const additionalParks = [park];
        const expectedCollection: IPark[] = [...additionalParks, ...parkCollection];
        spyOn(parkService, 'addParkToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(parkService.query).toHaveBeenCalled();
        expect(parkService.addParkToCollectionIfMissing).toHaveBeenCalledWith(parkCollection, ...additionalParks);
        expect(comp.parksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const equipement: IEquipement = { id: 456 };
        const park: IPark = { id: 15554 };
        equipement.park = park;

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(equipement));
        expect(comp.parksSharedCollection).toContain(park);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const equipement = { id: 123 };
        spyOn(equipementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: equipement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(equipementService.update).toHaveBeenCalledWith(equipement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const equipement = new Equipement();
        spyOn(equipementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: equipement }));
        saveSubject.complete();

        // THEN
        expect(equipementService.create).toHaveBeenCalledWith(equipement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const equipement = { id: 123 };
        spyOn(equipementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(equipementService.update).toHaveBeenCalledWith(equipement);
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
    });
  });
});
