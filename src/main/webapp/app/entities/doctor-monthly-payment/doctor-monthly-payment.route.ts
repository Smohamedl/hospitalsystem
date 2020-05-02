import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';
import { DoctorMonthlyPaymentService } from './doctor-monthly-payment.service';
import { DoctorMonthlyPaymentComponent } from './doctor-monthly-payment.component';
import { DoctorMonthlyPaymentDetailComponent } from './doctor-monthly-payment-detail.component';
import { DoctorMonthlyPaymentUpdateComponent } from './doctor-monthly-payment-update.component';
import { DoctorMonthlyPaymentDeletePopupComponent } from './doctor-monthly-payment-delete-dialog.component';
import { IDoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';

@Injectable({ providedIn: 'root' })
export class DoctorMonthlyPaymentResolve implements Resolve<IDoctorMonthlyPayment> {
  constructor(private service: DoctorMonthlyPaymentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDoctorMonthlyPayment> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((doctorMonthlyPayment: HttpResponse<DoctorMonthlyPayment>) => doctorMonthlyPayment.body));
    }
    return of(new DoctorMonthlyPayment());
  }
}

export const doctorMonthlyPaymentRoute: Routes = [
  {
    path: '',
    component: DoctorMonthlyPaymentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.doctorMonthlyPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DoctorMonthlyPaymentDetailComponent,
    resolve: {
      doctorMonthlyPayment: DoctorMonthlyPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorMonthlyPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DoctorMonthlyPaymentUpdateComponent,
    resolve: {
      doctorMonthlyPayment: DoctorMonthlyPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorMonthlyPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DoctorMonthlyPaymentUpdateComponent,
    resolve: {
      doctorMonthlyPayment: DoctorMonthlyPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorMonthlyPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const doctorMonthlyPaymentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DoctorMonthlyPaymentDeletePopupComponent,
    resolve: {
      doctorMonthlyPayment: DoctorMonthlyPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.doctorMonthlyPayment.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
