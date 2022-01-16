import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EquipementService } from '../service/equipement.service';

import { EquipementComponent } from './equipement.component';

describe('Component Tests', () => {
  describe('Equipement Management Component', () => {
    let comp: EquipementComponent;
    let fixture: ComponentFixture<EquipementComponent>;
    let service: EquipementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EquipementComponent],
      })
        .overrideTemplate(EquipementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EquipementComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EquipementService);

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
      expect(comp.equipements?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
