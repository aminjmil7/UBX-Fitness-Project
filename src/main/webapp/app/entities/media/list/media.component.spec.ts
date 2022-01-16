import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MediaService } from '../service/media.service';

import { MediaComponent } from './media.component';

describe('Component Tests', () => {
  describe('Media Management Component', () => {
    let comp: MediaComponent;
    let fixture: ComponentFixture<MediaComponent>;
    let service: MediaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MediaComponent],
      })
        .overrideTemplate(MediaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MediaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MediaService);

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
      expect(comp.media?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
