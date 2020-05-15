import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

type EntityResponseType = HttpResponse<ISocialOrganizationRegimen>;
type EntityArrayResponseType = HttpResponse<ISocialOrganizationRegimen[]>;

@Injectable({ providedIn: 'root' })
export class SocialOrganizationRegimenService {
  public resourceUrl = SERVER_API_URL + 'api/social-organization-regimen';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/social-organization-regimen';

  constructor(protected http: HttpClient) {}

  create(socialOrganizationRegimen: ISocialOrganizationRegimen): Observable<EntityResponseType> {
    return this.http.post<ISocialOrganizationRegimen>(this.resourceUrl, socialOrganizationRegimen, { observe: 'response' });
  }

  update(socialOrganizationRegimen: ISocialOrganizationRegimen): Observable<EntityResponseType> {
    return this.http.put<ISocialOrganizationRegimen>(this.resourceUrl, socialOrganizationRegimen, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISocialOrganizationRegimen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialOrganizationRegimen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialOrganizationRegimen[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
