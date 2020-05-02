import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IQuantityPrice } from 'app/shared/model/quantity-price.model';

type EntityResponseType = HttpResponse<IQuantityPrice>;
type EntityArrayResponseType = HttpResponse<IQuantityPrice[]>;

@Injectable({ providedIn: 'root' })
export class QuantityPriceService {
  public resourceUrl = SERVER_API_URL + 'api/quantity-prices';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/quantity-prices';

  constructor(protected http: HttpClient) {}

  create(quantityPrice: IQuantityPrice): Observable<EntityResponseType> {
    return this.http.post<IQuantityPrice>(this.resourceUrl, quantityPrice, { observe: 'response' });
  }

  update(quantityPrice: IQuantityPrice): Observable<EntityResponseType> {
    return this.http.put<IQuantityPrice>(this.resourceUrl, quantityPrice, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuantityPrice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuantityPrice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuantityPrice[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
