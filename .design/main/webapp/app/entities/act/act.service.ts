import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAct } from 'app/shared/model/act.model';

type EntityResponseType = HttpResponse<IAct>;
type EntityArrayResponseType = HttpResponse<IAct[]>;

@Injectable({ providedIn: 'root' })
export class ActService {
  public resourceUrl = SERVER_API_URL + 'api/acts';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/acts';

  constructor(protected http: HttpClient) {}

  create(act: IAct): Observable<EntityResponseType> {
    return this.http.post<IAct>(this.resourceUrl, act, { observe: 'response' });
  }

  update(act: IAct): Observable<EntityResponseType> {
    return this.http.put<IAct>(this.resourceUrl, act, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
