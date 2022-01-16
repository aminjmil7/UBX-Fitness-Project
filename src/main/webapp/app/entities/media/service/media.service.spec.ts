import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AuthType } from 'app/entities/enumerations/auth-type.model';
import { IMedia, Media } from '../media.model';

import { MediaService } from './media.service';

describe('Service Tests', () => {
  describe('Media Service', () => {
    let service: MediaService;
    let httpMock: HttpTestingController;
    let elemDefault: IMedia;
    let expectedResult: IMedia | IMedia[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MediaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fileName: 'AAAAAAA',
        filePath: 'AAAAAAA',
        fileType: 'AAAAAAA',
        authType: AuthType.LEARN,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Media', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Media()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Media', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fileName: 'BBBBBB',
            filePath: 'BBBBBB',
            fileType: 'BBBBBB',
            authType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Media', () => {
        const patchObject = Object.assign(
          {
            fileName: 'BBBBBB',
            fileType: 'BBBBBB',
          },
          new Media()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Media', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fileName: 'BBBBBB',
            filePath: 'BBBBBB',
            fileType: 'BBBBBB',
            authType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Media', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMediaToCollectionIfMissing', () => {
        it('should add a Media to an empty array', () => {
          const media: IMedia = { id: 123 };
          expectedResult = service.addMediaToCollectionIfMissing([], media);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(media);
        });

        it('should not add a Media to an array that contains it', () => {
          const media: IMedia = { id: 123 };
          const mediaCollection: IMedia[] = [
            {
              ...media,
            },
            { id: 456 },
          ];
          expectedResult = service.addMediaToCollectionIfMissing(mediaCollection, media);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Media to an array that doesn't contain it", () => {
          const media: IMedia = { id: 123 };
          const mediaCollection: IMedia[] = [{ id: 456 }];
          expectedResult = service.addMediaToCollectionIfMissing(mediaCollection, media);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(media);
        });

        it('should add only unique Media to an array', () => {
          const mediaArray: IMedia[] = [{ id: 123 }, { id: 456 }, { id: 18206 }];
          const mediaCollection: IMedia[] = [{ id: 123 }];
          expectedResult = service.addMediaToCollectionIfMissing(mediaCollection, ...mediaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const media: IMedia = { id: 123 };
          const media2: IMedia = { id: 456 };
          expectedResult = service.addMediaToCollectionIfMissing([], media, media2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(media);
          expect(expectedResult).toContain(media2);
        });

        it('should accept null and undefined values', () => {
          const media: IMedia = { id: 123 };
          expectedResult = service.addMediaToCollectionIfMissing([], null, media, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(media);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
