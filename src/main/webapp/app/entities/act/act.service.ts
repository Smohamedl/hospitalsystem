import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAct } from 'app/shared/model/act.model';
import { HttpHeaders} from '@angular/common/http';

type EntityResponseType = HttpResponse<IAct>;
type EntityResponseBlobType = HttpResponse<Blob>;
type EntityArrayResponseType = HttpResponse<IAct[]>;

@Injectable({ providedIn: 'root' })
export class ActService {
  public resourceUrl = SERVER_API_URL + 'api/acts';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/acts';

  constructor(protected http: HttpClient) {}

  create(act: IAct): Observable<EntityResponseBlobType> {
	let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/pdf; charset=utf-8');

    return this.http.post(this.resourceUrl, act, {headers,
      observe: 'response',
      responseType: 'blob' });
  }

  update(act: IAct): Observable<EntityResponseType> {
    return this.http.put<IAct>(this.resourceUrl, act, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByService(_service: string): Observable<EntityArrayResponseType> {
    return this.http.get<IAct[]>(`${this.resourceUrl}/service/${_service}`, { observe: 'response' });
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
