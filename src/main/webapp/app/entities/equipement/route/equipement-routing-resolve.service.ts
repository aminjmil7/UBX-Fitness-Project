import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEquipement, Equipement } from '../equipement.model';
import { EquipementService } from '../service/equipement.service';

@Injectable({ providedIn: 'root' })
export class EquipementRoutingResolveService implements Resolve<IEquipement> {
  constructor(protected service: EquipementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEquipement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((equipement: HttpResponse<Equipement>) => {
          if (equipement.body) {
            return of(equipement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Equipement());
  }
}
