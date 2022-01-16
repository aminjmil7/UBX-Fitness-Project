jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMedia, Media } from '../media.model';
import { MediaService } from '../service/media.service';

import { MediaRoutingResolveService } from './media-routing-resolve.service';

describe('Service Tests', () => {
  describe('Media routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MediaRoutingResolveService;
    let service: MediaService;
    let resultMedia: IMedia | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MediaRoutingResolveService);
      service = TestBed.inject(MediaService);
      resultMedia = undefined;
    });

    describe('resolve', () => {
      it('should return IMedia returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedia = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedia).toEqual({ id: 123 });
      });

      it('should return new IMedia if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedia = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMedia).toEqual(new Media());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedia = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedia).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
