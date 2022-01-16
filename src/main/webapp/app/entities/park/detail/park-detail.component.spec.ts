import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParkDetailComponent } from './park-detail.component';

describe('Component Tests', () => {
  describe('Park Management Detail Component', () => {
    let comp: ParkDetailComponent;
    let fixture: ComponentFixture<ParkDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ParkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ park: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ParkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load park on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.park).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
