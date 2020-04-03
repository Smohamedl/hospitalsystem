import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISocialOrganization } from 'app/shared/model/social-organization.model';

type EntityResponseType = HttpResponse<ISocialOrganization>;
type EntityArrayResponseType = HttpResponse<ISocialOrganization[]>;

@Injectable({ providedIn: 'root' })
export class SocialOrganizationService {
  public resourceUrl = SERVER_API_URL + 'api/social-organizations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/social-organizations';

  constructor(protected http: HttpClient) {}

  create(socialOrganization: ISocialOrganization): Observable<EntityResponseType> {
    return this.http.post<ISocialOrganization>(this.resourceUrl, socialOrganization, { observe: 'response' });
  }

  update(socialOrganization: ISocialOrganization): Observable<EntityResponseType> {
    return this.http.put<ISocialOrganization>(this.resourceUrl, socialOrganization, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISocialOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialOrganization[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
