jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEvents, Events } from '../events.model';
import { EventsService } from '../service/events.service';

import { EventsRoutingResolveService } from './events-routing-resolve.service';

describe('Service Tests', () => {
  describe('Events routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EventsRoutingResolveService;
    let service: EventsService;
    let resultEvents: IEvents | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EventsRoutingResolveService);
      service = TestBed.inject(EventsService);
      resultEvents = undefined;
    });

    describe('resolve', () => {
      it('should return IEvents returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEvents = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEvents).toEqual({ id: 123 });
      });

      it('should return new IEvents if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEvents = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEvents).toEqual(new Events());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEvents = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEvents).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
