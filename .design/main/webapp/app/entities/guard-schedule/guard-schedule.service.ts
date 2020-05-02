import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';

type EntityResponseType = HttpResponse<IGuardSchedule>;
type EntityArrayResponseType = HttpResponse<IGuardSchedule[]>;

@Injectable({ providedIn: 'root' })
export class GuardScheduleService {
  public resourceUrl = SERVER_API_URL + 'api/guard-schedules';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/guard-schedules';

  constructor(protected http: HttpClient) {}

  create(guardSchedule: IGuardSchedule): Observable<EntityResponseType> {
    return this.http.post<IGuardSchedule>(this.resourceUrl, guardSchedule, { observe: 'response' });
  }

  update(guardSchedule: IGuardSchedule): Observable<EntityResponseType> {
    return this.http.put<IGuardSchedule>(this.resourceUrl, guardSchedule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuardSchedule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuardSchedule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuardSchedule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
