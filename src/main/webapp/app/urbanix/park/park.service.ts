import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPark, getParkIdentifier } from './park.model';

export type EntityResponseType = HttpResponse<IPark>;
export type EntityArrayResponseType = HttpResponse<IPark[]>;

@Injectable({ providedIn: 'root' })
export class ParkService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/parks');
  public resourceUrlTwo = this.applicationConfigService.getEndpointFor('api/parksByDistance/');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getParksByDistance(distanceMax: number, lat: number, lon: number): Observable<EntityArrayResponseType> {
    return this.http.get<IPark[]>(this.resourceUrlTwo + `${distanceMax},${lat},${lon}`, { observe: 'response' });
  }

  create(park: IPark): Observable<EntityResponseType> {
    return this.http.post<IPark>(this.resourceUrl, park, { observe: 'response' });
  }

  update(park: IPark): Observable<EntityResponseType> {
    return this.http.put<IPark>(`${this.resourceUrl}/${getParkIdentifier(park) as number}`, park, { observe: 'response' });
  }

  partialUpdate(park: IPark): Observable<EntityResponseType> {
    return this.http.patch<IPark>(`${this.resourceUrl}/${getParkIdentifier(park) as number}`, park, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPark>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPark[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
