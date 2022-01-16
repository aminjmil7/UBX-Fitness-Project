import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedia, getMediaIdentifier } from './media.model';

export type EntityResponseType = HttpResponse<IMedia>;
export type EntityArrayResponseType = HttpResponse<IMedia[]>;

@Injectable({ providedIn: 'root' })
export class MediaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/media');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(media: IMedia): Observable<EntityResponseType> {
    return this.http.post<IMedia>(this.resourceUrl, media, { observe: 'response' });
  }

  update(media: IMedia): Observable<EntityResponseType> {
    return this.http.put<IMedia>(`${this.resourceUrl}/${getMediaIdentifier(media) as number}`, media, { observe: 'response' });
  }

  partialUpdate(media: IMedia): Observable<EntityResponseType> {
    return this.http.patch<IMedia>(`${this.resourceUrl}/${getMediaIdentifier(media) as number}`, media, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMediaToCollectionIfMissing(mediaCollection: IMedia[], ...mediaToCheck: (IMedia | null | undefined)[]): IMedia[] {
    const media: IMedia[] = mediaToCheck.filter(isPresent);
    if (media.length > 0) {
      const mediaCollectionIdentifiers = mediaCollection.map(mediaItem => getMediaIdentifier(mediaItem)!);
      const mediaToAdd = media.filter(mediaItem => {
        const mediaIdentifier = getMediaIdentifier(mediaItem);
        if (mediaIdentifier == null || mediaCollectionIdentifiers.includes(mediaIdentifier)) {
          return false;
        }
        mediaCollectionIdentifiers.push(mediaIdentifier);
        return true;
      });
      return [...mediaToAdd, ...mediaCollection];
    }
    return mediaCollection;
  }
}
