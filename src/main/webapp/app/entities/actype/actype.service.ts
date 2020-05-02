import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IActype } from 'app/shared/model/actype.model';

type EntityResponseType = HttpResponse<IActype>;
type EntityArrayResponseType = HttpResponse<IActype[]>;

@Injectable({ providedIn: 'root' })
export class ActypeService {
  public resourceUrl = SERVER_API_URL + 'api/actypes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/actypes';

  constructor(protected http: HttpClient) {}

  create(actype: IActype): Observable<EntityResponseType> {
    return this.http.post<IActype>(this.resourceUrl, actype, { observe: 'response' });
  }

  update(actype: IActype): Observable<EntityResponseType> {
    return this.http.put<IActype>(this.resourceUrl, actype, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActype>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  
  findByService(_service: string):  Observable<EntityArrayResponseType>{
    return this.http.get<IActype[]>(`${this.resourceUrl}/service/${_service}`, {observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActype[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActype[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
