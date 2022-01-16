import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPark, Park } from '../park.model';

import { ParkService } from './park.service';

describe('Service Tests', () => {
  describe('Park Service', () => {
    let service: ParkService;
    let httpMock: HttpTestingController;
    let elemDefault: IPark;
    let expectedResult: IPark | IPark[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ParkService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        parkName: 'AAAAAAA',
        parkAddress: 'AAAAAAA',
        longtitude: 0,
        latitude: 0,
        verified: false,
        dateInstall: currentDate,
        dateOpen: currentDate,
        dateClose: currentDate,
        note: 'AAAAAAA',
        reseller: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateInstall: currentDate.format(DATE_TIME_FORMAT),
            dateOpen: currentDate.format(DATE_TIME_FORMAT),
            dateClose: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Park', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateInstall: currentDate.format(DATE_TIME_FORMAT),
            dateOpen: currentDate.format(DATE_TIME_FORMAT),
            dateClose: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateInstall: currentDate,
            dateOpen: currentDate,
            dateClose: currentDate,
          },
          returnedFromService
        );

        service.create(new Park()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Park', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            parkName: 'BBBBBB',
            parkAddress: 'BBBBBB',
            longtitude: 1,
            latitude: 1,
            verified: true,
            dateInstall: currentDate.format(DATE_TIME_FORMAT),
            dateOpen: currentDate.format(DATE_TIME_FORMAT),
            dateClose: currentDate.format(DATE_TIME_FORMAT),
            note: 'BBBBBB',
            reseller: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateInstall: currentDate,
            dateOpen: currentDate,
            dateClose: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Park', () => {
        const patchObject = Object.assign(
          {
            parkName: 'BBBBBB',
            parkAddress: 'BBBBBB',
          },
          new Park()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateInstall: currentDate,
            dateOpen: currentDate,
            dateClose: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Park', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            parkName: 'BBBBBB',
            parkAddress: 'BBBBBB',
            longtitude: 1,
            latitude: 1,
            verified: true,
            dateInstall: currentDate.format(DATE_TIME_FORMAT),
            dateOpen: currentDate.format(DATE_TIME_FORMAT),
            dateClose: currentDate.format(DATE_TIME_FORMAT),
            note: 'BBBBBB',
            reseller: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateInstall: currentDate,
            dateOpen: currentDate,
            dateClose: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Park', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addParkToCollectionIfMissing', () => {
        it('should add a Park to an empty array', () => {
          const park: IPark = { id: 123 };
          expectedResult = service.addParkToCollectionIfMissing([], park);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(park);
        });

        it('should not add a Park to an array that contains it', () => {
          const park: IPark = { id: 123 };
          const parkCollection: IPark[] = [
            {
              ...park,
            },
            { id: 456 },
          ];
          expectedResult = service.addParkToCollectionIfMissing(parkCollection, park);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Park to an array that doesn't contain it", () => {
          const park: IPark = { id: 123 };
          const parkCollection: IPark[] = [{ id: 456 }];
          expectedResult = service.addParkToCollectionIfMissing(parkCollection, park);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(park);
        });

        it('should add only unique Park to an array', () => {
          const parkArray: IPark[] = [{ id: 123 }, { id: 456 }, { id: 72294 }];
          const parkCollection: IPark[] = [{ id: 123 }];
          expectedResult = service.addParkToCollectionIfMissing(parkCollection, ...parkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const park: IPark = { id: 123 };
          const park2: IPark = { id: 456 };
          expectedResult = service.addParkToCollectionIfMissing([], park, park2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(park);
          expect(expectedResult).toContain(park2);
        });

        it('should accept null and undefined values', () => {
          const park: IPark = { id: 123 };
          expectedResult = service.addParkToCollectionIfMissing([], null, park, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(park);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
