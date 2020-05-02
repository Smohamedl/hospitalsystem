import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';

type EntityResponseType = HttpResponse<IDoctorPartPayment>;
type EntityArrayResponseType = HttpResponse<IDoctorPartPayment[]>;

@Injectable({ providedIn: 'root' })
export class DoctorPartPaymentService {
  public resourceUrl = SERVER_API_URL + 'api/doctor-part-payments';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/doctor-part-payments';

  constructor(protected http: HttpClient) {}

  create(doctorPartPayment: IDoctorPartPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorPartPayment);
    return this.http
      .post<IDoctorPartPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(doctorPartPayment: IDoctorPartPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorPartPayment);
    return this.http
      .put<IDoctorPartPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDoctorPartPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDoctorPartPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDoctorPartPayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(doctorPartPayment: IDoctorPartPayment): IDoctorPartPayment {
    const copy: IDoctorPartPayment = Object.assign({}, doctorPartPayment, {
      date: doctorPartPayment.date != null && doctorPartPayment.date.isValid() ? doctorPartPayment.date.format(DATE_FORMAT) : null
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
      res.body.forEach((doctorPartPayment: IDoctorPartPayment) => {
        doctorPartPayment.date = doctorPartPayment.date != null ? moment(doctorPartPayment.date) : null;
      });
    }
    return res;
  }
}
