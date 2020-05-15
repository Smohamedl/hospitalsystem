import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProvider } from 'app/shared/model/provider.model';

type EntityResponseType = HttpResponse<IProvider>;
type EntityArrayResponseType = HttpResponse<IProvider[]>;

@Injectable({ providedIn: 'root' })
export class ProviderService {
  public resourceUrl = SERVER_API_URL + 'api/provider';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/provider';

  constructor(protected http: HttpClient) {}

  create(provider: IProvider): Observable<EntityResponseType> {
    return this.http.post<IProvider>(this.resourceUrl, provider, { observe: 'response' });
  }

  update(provider: IProvider): Observable<EntityResponseType> {
    return this.http.put<IProvider>(this.resourceUrl, provider, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvider[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
