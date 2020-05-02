import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';
import { DoctorPartPaymentService } from './doctor-part-payment.service';
import { DoctorPartPaymentComponent } from './doctor-part-payment.component';
import { DoctorPartPaymentDetailComponent } from './doctor-part-payment-detail.component';
import { DoctorPartPaymentUpdateComponent } from './doctor-part-payment-update.component';
import { DoctorPartPaymentDeletePopupComponent } from './doctor-part-payment-delete-dialog.component';
import { IDoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';

@Injectable({ providedIn: 'root' })
export class DoctorPartPaymentResolve implements Resolve<IDoctorPartPayment> {
  constructor(private service: DoctorPartPaymentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDoctorPartPayment> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((doctorPartPayment: HttpResponse<DoctorPartPayment>) => doctorPartPayment.body));
    }
    return of(new DoctorPartPayment());
  }
}

export const doctorPartPaymentRoute: Routes = [
  {
    path: '',
    component: DoctorPartPaymentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.doctorPartPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DoctorPartPaymentDetailComponent,
    resolve: {
      doctorPartPayment: DoctorPartPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorPartPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DoctorPartPaymentUpdateComponent,
    resolve: {
      doctorPartPayment: DoctorPartPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorPartPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DoctorPartPaymentUpdateComponent,
    resolve: {
      doctorPartPayment: DoctorPartPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorPartPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const doctorPartPaymentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DoctorPartPaymentDeletePopupComponent,
    resolve: {
      doctorPartPayment: DoctorPartPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorPartPayment.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
