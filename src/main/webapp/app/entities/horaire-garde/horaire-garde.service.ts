import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHoraire_garde } from 'app/shared/model/horaire-garde.model';

type EntityResponseType = HttpResponse<IHoraire_garde>;
type EntityArrayResponseType = HttpResponse<IHoraire_garde[]>;

@Injectable({ providedIn: 'root' })
export class Horaire_gardeService {
  public resourceUrl = SERVER_API_URL + 'api/horaire-gardes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/horaire-gardes';

  constructor(protected http: HttpClient) {}

  create(horaire_garde: IHoraire_garde): Observable<EntityResponseType> {
    return this.http.post<IHoraire_garde>(this.resourceUrl, horaire_garde, { observe: 'response' });
  }

  update(horaire_garde: IHoraire_garde): Observable<EntityResponseType> {
    return this.http.put<IHoraire_garde>(this.resourceUrl, horaire_garde, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHoraire_garde>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHoraire_garde[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHoraire_garde[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
