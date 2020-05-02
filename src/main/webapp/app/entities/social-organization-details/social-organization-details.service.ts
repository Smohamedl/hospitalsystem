import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';

type EntityResponseType = HttpResponse<ISocialOrganizationDetails>;
type EntityArrayResponseType = HttpResponse<ISocialOrganizationDetails[]>;

@Injectable({ providedIn: 'root' })
export class SocialOrganizationDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/social-organization-details';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/social-organization-details';

  constructor(protected http: HttpClient) {}

  create(socialOrganizationDetails: ISocialOrganizationDetails): Observable<EntityResponseType> {
    return this.http.post<ISocialOrganizationDetails>(this.resourceUrl, socialOrganizationDetails, { observe: 'response' });
  }

  update(socialOrganizationDetails: ISocialOrganizationDetails): Observable<EntityResponseType> {
    return this.http.put<ISocialOrganizationDetails>(this.resourceUrl, socialOrganizationDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISocialOrganizationDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialOrganizationDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialOrganizationDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
