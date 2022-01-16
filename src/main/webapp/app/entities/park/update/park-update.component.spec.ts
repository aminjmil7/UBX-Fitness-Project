jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ParkService } from '../service/park.service';
import { IPark, Park } from '../park.model';

import { ParkUpdateComponent } from './park-update.component';

describe('Component Tests', () => {
  describe('Park Management Update Component', () => {
    let comp: ParkUpdateComponent;
    let fixture: ComponentFixture<ParkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let parkService: ParkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ParkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      parkService = TestBed.inject(ParkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const park: IPark = { id: 456 };

        activatedRoute.data = of({ park });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(park));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const park = { id: 123 };
        spyOn(parkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ park });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: park }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(parkService.update).toHaveBeenCalledWith(park);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const park = new Park();
        spyOn(parkService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ park });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: park }));
        saveSubject.complete();

        // THEN
        expect(parkService.create).toHaveBeenCalledWith(park);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const park = { id: 123 };
        spyOn(parkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ park });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(parkService.update).toHaveBeenCalledWith(park);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
