import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvents, getEventsIdentifier } from './events.model';

export type EntityResponseType = HttpResponse<IEvents>;
export type EntityArrayResponseType = HttpResponse<IEvents[]>;

@Injectable({ providedIn: 'root' })
export class EventsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/events');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(events: IEvents): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(events);
    return this.http
      .post<IEvents>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(events: IEvents): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(events);
    return this.http
      .put<IEvents>(`${this.resourceUrl}/${getEventsIdentifier(events) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(events: IEvents): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(events);
    return this.http
      .patch<IEvents>(`${this.resourceUrl}/${getEventsIdentifier(events) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvents>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvents[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventsToCollectionIfMissing(eventsCollection: IEvents[], ...eventsToCheck: (IEvents | null | undefined)[]): IEvents[] {
    const events: IEvents[] = eventsToCheck.filter(isPresent);
    if (events.length > 0) {
      const eventsCollectionIdentifiers = eventsCollection.map(eventsItem => getEventsIdentifier(eventsItem)!);
      const eventsToAdd = events.filter(eventsItem => {
        const eventsIdentifier = getEventsIdentifier(eventsItem);
        if (eventsIdentifier == null || eventsCollectionIdentifiers.includes(eventsIdentifier)) {
          return false;
        }
        eventsCollectionIdentifiers.push(eventsIdentifier);
        return true;
      });
      return [...eventsToAdd, ...eventsCollection];
    }
    return eventsCollection;
  }

  protected convertDateFromClient(events: IEvents): IEvents {
    return Object.assign({}, events, {
      eventDate: events.eventDate?.isValid() ? events.eventDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.eventDate = res.body.eventDate ? dayjs(res.body.eventDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((events: IEvents) => {
        events.eventDate = events.eventDate ? dayjs(events.eventDate) : undefined;
      });
    }
    return res;
  }
}
