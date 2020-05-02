import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';

type EntityResponseType = HttpResponse<IFixedDoctorPayment>;
type EntityArrayResponseType = HttpResponse<IFixedDoctorPayment[]>;

@Injectable({ providedIn: 'root' })
export class FixedDoctorPaymentService {
  public resourceUrl = SERVER_API_URL + 'api/fixed-doctor-payments';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/fixed-doctor-payments';

  constructor(protected http: HttpClient) {}

  create(fixedDoctorPayment: IFixedDoctorPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedDoctorPayment);
    return this.http
      .post<IFixedDoctorPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedDoctorPayment: IFixedDoctorPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedDoctorPayment);
    return this.http
      .put<IFixedDoctorPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFixedDoctorPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedDoctorPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedDoctorPayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(fixedDoctorPayment: IFixedDoctorPayment): IFixedDoctorPayment {
    const copy: IFixedDoctorPayment = Object.assign({}, fixedDoctorPayment, {
      date: fixedDoctorPayment.date != null && fixedDoctorPayment.date.isValid() ? fixedDoctorPayment.date.format(DATE_FORMAT) : null
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
      res.body.forEach((fixedDoctorPayment: IFixedDoctorPayment) => {
        fixedDoctorPayment.date = fixedDoctorPayment.date != null ? moment(fixedDoctorPayment.date) : null;
      });
    }
    return res;
  }
}
