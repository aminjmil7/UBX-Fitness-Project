jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEquipement, Equipement } from '../equipement.model';
import { EquipementService } from '../service/equipement.service';

import { EquipementRoutingResolveService } from './equipement-routing-resolve.service';

describe('Service Tests', () => {
  describe('Equipement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EquipementRoutingResolveService;
    let service: EquipementService;
    let resultEquipement: IEquipement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EquipementRoutingResolveService);
      service = TestBed.inject(EquipementService);
      resultEquipement = undefined;
    });

    describe('resolve', () => {
      it('should return IEquipement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEquipement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEquipement).toEqual({ id: 123 });
      });

      it('should return new IEquipement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEquipement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEquipement).toEqual(new Equipement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEquipement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEquipement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
