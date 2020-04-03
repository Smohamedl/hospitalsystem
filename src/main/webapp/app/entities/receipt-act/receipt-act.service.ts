import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IReceiptAct } from 'app/shared/model/receipt-act.model';
import { HttpHeaders} from '@angular/common/http';

type EntityResponseType = HttpResponse<IReceiptAct>;
type EntityArrayResponseType = HttpResponse<IReceiptAct[]>;

@Injectable({ providedIn: 'root' })
export class ReceiptActService {
  public resourceUrl = SERVER_API_URL + 'api/receipt-acts';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/receipt-acts';

  constructor(protected http: HttpClient) {}

  create(receiptAct: IReceiptAct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receiptAct);
    return this.http
      .post<IReceiptAct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(receiptAct: IReceiptAct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receiptAct);
    return this.http
      .put<IReceiptAct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReceiptAct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReceiptAct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  
  download(id: number): Observable<HttpResponse<any>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/pdf; charset=utf-8');
    return this.http.get<any>(`${this.resourceUrl}/download/${id}`, {headers,
      observe: 'response',
      responseType: 'blob' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReceiptAct[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findByDoctor(req: any, _doctor: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReceiptAct[]>(`${this.resourceUrl}/doctor/${_doctor}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  
  searchByDoctor(req: any, _doctor: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReceiptAct[]>(`${this.resourceSearchUrl}/doctor/${_doctor}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(receiptAct: IReceiptAct): IReceiptAct {
    const copy: IReceiptAct = Object.assign({}, receiptAct, {
      date: receiptAct.date != null && receiptAct.date.isValid() ? receiptAct.date.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((receiptAct: IReceiptAct) => {
        receiptAct.date = receiptAct.date != null ? moment(receiptAct.date) : null;
      });
    }
    return res;
  }
}
