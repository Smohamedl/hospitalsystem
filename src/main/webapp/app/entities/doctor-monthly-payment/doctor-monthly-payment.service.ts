import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';

type EntityResponseType = HttpResponse<IDoctorMonthlyPayment>;
type EntityArrayResponseType = HttpResponse<IDoctorMonthlyPayment[]>;

@Injectable({ providedIn: 'root' })
export class DoctorMonthlyPaymentService {
  public resourceUrl = SERVER_API_URL + 'api/doctor-monthly-payments';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/doctor-monthly-payments';

  constructor(protected http: HttpClient) {}

  create(doctorMonthlyPayment: IDoctorMonthlyPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorMonthlyPayment);
    return this.http
      .post<IDoctorMonthlyPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(doctorMonthlyPayment: IDoctorMonthlyPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorMonthlyPayment);
    return this.http
      .put<IDoctorMonthlyPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDoctorMonthlyPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDoctorMonthlyPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDoctorMonthlyPayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(doctorMonthlyPayment: IDoctorMonthlyPayment): IDoctorMonthlyPayment {
    const copy: IDoctorMonthlyPayment = Object.assign({}, doctorMonthlyPayment, {
      date: doctorMonthlyPayment.date != null && doctorMonthlyPayment.date.isValid() ? doctorMonthlyPayment.date.format(DATE_FORMAT) : null
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
      res.body.forEach((doctorMonthlyPayment: IDoctorMonthlyPayment) => {
        doctorMonthlyPayment.date = doctorMonthlyPayment.date != null ? moment(doctorMonthlyPayment.date) : null;
      });
    }
    return res;
  }
}
