import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHospitalization } from 'app/shared/model/hospitalization.model';

type EntityResponseType = HttpResponse<IHospitalization>;
type EntityArrayResponseType = HttpResponse<IHospitalization[]>;

@Injectable({ providedIn: 'root' })
export class HospitalizationService {
  public resourceUrl = SERVER_API_URL + 'api/hospitalizations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/hospitalizations';

  constructor(protected http: HttpClient) {}

  create(hospitalization: IHospitalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hospitalization);
    return this.http
      .post<IHospitalization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(hospitalization: IHospitalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hospitalization);
    return this.http
      .put<IHospitalization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHospitalization>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHospitalization[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHospitalization[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(hospitalization: IHospitalization): IHospitalization {
    const copy: IHospitalization = Object.assign({}, hospitalization, {
      date: hospitalization.date != null && hospitalization.date.isValid() ? hospitalization.date.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((hospitalization: IHospitalization) => {
        hospitalization.date = hospitalization.date != null ? moment(hospitalization.date) : null;
      });
    }
    return res;
  }
}
