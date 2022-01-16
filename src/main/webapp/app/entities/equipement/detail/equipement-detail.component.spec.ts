import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EquipementDetailComponent } from './equipement-detail.component';

describe('Component Tests', () => {
  describe('Equipement Management Detail Component', () => {
    let comp: EquipementDetailComponent;
    let fixture: ComponentFixture<EquipementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EquipementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ equipement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EquipementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EquipementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load equipement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.equipement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
