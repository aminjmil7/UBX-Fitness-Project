import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPark, getParkIdentifier } from '../park.model';

export type EntityResponseType = HttpResponse<IPark>;
export type EntityArrayResponseType = HttpResponse<IPark[]>;

@Injectable({ providedIn: 'root' })
export class ParkService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/parks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(park: IPark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(park);
    return this.http
      .post<IPark>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(park: IPark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(park);
    return this.http
      .put<IPark>(`${this.resourceUrl}/${getParkIdentifier(park) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(park: IPark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(park);
    return this.http
      .patch<IPark>(`${this.resourceUrl}/${getParkIdentifier(park) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPark>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPark[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParkToCollectionIfMissing(parkCollection: IPark[], ...parksToCheck: (IPark | null | undefined)[]): IPark[] {
    const parks: IPark[] = parksToCheck.filter(isPresent);
    if (parks.length > 0) {
      const parkCollectionIdentifiers = parkCollection.map(parkItem => getParkIdentifier(parkItem)!);
      const parksToAdd = parks.filter(parkItem => {
        const parkIdentifier = getParkIdentifier(parkItem);
        if (parkIdentifier == null || parkCollectionIdentifiers.includes(parkIdentifier)) {
          return false;
        }
        parkCollectionIdentifiers.push(parkIdentifier);
        return true;
      });
      return [...parksToAdd, ...parkCollection];
    }
    return parkCollection;
  }

  protected convertDateFromClient(park: IPark): IPark {
    return Object.assign({}, park, {
      dateInstall: park.dateInstall?.isValid() ? park.dateInstall.toJSON() : undefined,
      dateOpen: park.dateOpen?.isValid() ? park.dateOpen.toJSON() : undefined,
      dateClose: park.dateClose?.isValid() ? park.dateClose.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateInstall = res.body.dateInstall ? dayjs(res.body.dateInstall) : undefined;
      res.body.dateOpen = res.body.dateOpen ? dayjs(res.body.dateOpen) : undefined;
      res.body.dateClose = res.body.dateClose ? dayjs(res.body.dateClose) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((park: IPark) => {
        park.dateInstall = park.dateInstall ? dayjs(park.dateInstall) : undefined;
        park.dateOpen = park.dateOpen ? dayjs(park.dateOpen) : undefined;
        park.dateClose = park.dateClose ? dayjs(park.dateClose) : undefined;
      });
    }
    return res;
  }
}
