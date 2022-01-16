jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPark, Park } from '../park.model';
import { ParkService } from '../service/park.service';

import { ParkRoutingResolveService } from './park-routing-resolve.service';

describe('Service Tests', () => {
  describe('Park routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ParkRoutingResolveService;
    let service: ParkService;
    let resultPark: IPark | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ParkRoutingResolveService);
      service = TestBed.inject(ParkService);
      resultPark = undefined;
    });

    describe('resolve', () => {
      it('should return IPark returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPark = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPark).toEqual({ id: 123 });
      });

      it('should return new IPark if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPark = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPark).toEqual(new Park());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPark = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPark).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
