import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReport, getReportIdentifier } from './report.model';

export type EntityResponseType = HttpResponse<IReport>;
export type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/reports');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(report: IReport): Observable<EntityResponseType> {
    return this.http.post<IReport>(this.resourceUrl, report, { observe: 'response' });
  }

  update(report: IReport): Observable<EntityResponseType> {
    return this.http.put<IReport>(`${this.resourceUrl}/${getReportIdentifier(report) as number}`, report, { observe: 'response' });
  }

  partialUpdate(report: IReport): Observable<EntityResponseType> {
    return this.http.patch<IReport>(`${this.resourceUrl}/${getReportIdentifier(report) as number}`, report, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReportToCollectionIfMissing(reportCollection: IReport[], ...reportsToCheck: (IReport | null | undefined)[]): IReport[] {
    const reports: IReport[] = reportsToCheck.filter(isPresent);
    if (reports.length > 0) {
      const reportCollectionIdentifiers = reportCollection.map(reportItem => getReportIdentifier(reportItem)!);
      const reportsToAdd = reports.filter(reportItem => {
        const reportIdentifier = getReportIdentifier(reportItem);
        if (reportIdentifier == null || reportCollectionIdentifiers.includes(reportIdentifier)) {
          return false;
        }
        reportCollectionIdentifiers.push(reportIdentifier);
        return true;
      });
      return [...reportsToAdd, ...reportCollection];
    }
    return reportCollection;
  }
}
