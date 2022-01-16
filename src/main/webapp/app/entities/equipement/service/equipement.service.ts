import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEquipement, getEquipementIdentifier } from '../equipement.model';

export type EntityResponseType = HttpResponse<IEquipement>;
export type EntityArrayResponseType = HttpResponse<IEquipement[]>;

@Injectable({ providedIn: 'root' })
export class EquipementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/equipements');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(equipement: IEquipement): Observable<EntityResponseType> {
    return this.http.post<IEquipement>(this.resourceUrl, equipement, { observe: 'response' });
  }

  update(equipement: IEquipement): Observable<EntityResponseType> {
    return this.http.put<IEquipement>(`${this.resourceUrl}/${getEquipementIdentifier(equipement) as number}`, equipement, {
      observe: 'response',
    });
  }

  partialUpdate(equipement: IEquipement): Observable<EntityResponseType> {
    return this.http.patch<IEquipement>(`${this.resourceUrl}/${getEquipementIdentifier(equipement) as number}`, equipement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEquipement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquipement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEquipementToCollectionIfMissing(
    equipementCollection: IEquipement[],
    ...equipementsToCheck: (IEquipement | null | undefined)[]
  ): IEquipement[] {
    const equipements: IEquipement[] = equipementsToCheck.filter(isPresent);
    if (equipements.length > 0) {
      const equipementCollectionIdentifiers = equipementCollection.map(equipementItem => getEquipementIdentifier(equipementItem)!);
      const equipementsToAdd = equipements.filter(equipementItem => {
        const equipementIdentifier = getEquipementIdentifier(equipementItem);
        if (equipementIdentifier == null || equipementCollectionIdentifiers.includes(equipementIdentifier)) {
          return false;
        }
        equipementCollectionIdentifiers.push(equipementIdentifier);
        return true;
      });
      return [...equipementsToAdd, ...equipementCollection];
    }
    return equipementCollection;
  }
}
