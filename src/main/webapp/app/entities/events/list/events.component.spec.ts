import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EventsService } from '../service/events.service';

import { EventsComponent } from './events.component';

describe('Component Tests', () => {
  describe('Events Management Component', () => {
    let comp: EventsComponent;
    let fixture: ComponentFixture<EventsComponent>;
    let service: EventsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventsComponent],
      })
        .overrideTemplate(EventsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EventsService);

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
      expect(comp.events?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
