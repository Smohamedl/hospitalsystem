import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGuard } from 'app/shared/model/guard.model';

type EntityResponseType = HttpResponse<IGuard>;
type EntityArrayResponseType = HttpResponse<IGuard[]>;

@Injectable({ providedIn: 'root' })
export class GuardService {
  public resourceUrl = SERVER_API_URL + 'api/guards';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/guards';

  constructor(protected http: HttpClient) {}

  create(guard: IGuard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(guard);
    return this.http
      .post<IGuard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(guard: IGuard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(guard);
    return this.http
      .put<IGuard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGuard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGuard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGuard[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(guard: IGuard): IGuard {
    const copy: IGuard = Object.assign({}, guard, {
      date: guard.date != null && guard.date.isValid() ? guard.date.format(DATE_FORMAT) : null
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
      res.body.forEach((guard: IGuard) => {
        guard.date = guard.date != null ? moment(guard.date) : null;
      });
    }
    return res;
  }
}
