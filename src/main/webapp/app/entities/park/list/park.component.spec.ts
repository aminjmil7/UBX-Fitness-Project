import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ParkService } from '../service/park.service';

import { ParkComponent } from './park.component';

describe('Component Tests', () => {
  describe('Park Management Component', () => {
    let comp: ParkComponent;
    let fixture: ComponentFixture<ParkComponent>;
    let service: ParkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParkComponent],
      })
        .overrideTemplate(ParkComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParkComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ParkService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.parks?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
