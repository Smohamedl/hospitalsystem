import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';
import { FixedDoctorPaymentService } from './fixed-doctor-payment.service';
import { FixedDoctorPaymentComponent } from './fixed-doctor-payment.component';
import { FixedDoctorPaymentDetailComponent } from './fixed-doctor-payment-detail.component';
import { FixedDoctorPaymentUpdateComponent } from './fixed-doctor-payment-update.component';
import { FixedDoctorPaymentDeletePopupComponent } from './fixed-doctor-payment-delete-dialog.component';
import { IFixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';

@Injectable({ providedIn: 'root' })
export class FixedDoctorPaymentResolve implements Resolve<IFixedDoctorPayment> {
  constructor(private service: FixedDoctorPaymentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedDoctorPayment> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((fixedDoctorPayment: HttpResponse<FixedDoctorPayment>) => fixedDoctorPayment.body));
    }
    return of(new FixedDoctorPayment());
  }
}

export const fixedDoctorPaymentRoute: Routes = [
  {
    path: '',
    component: FixedDoctorPaymentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.fixedDoctorPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FixedDoctorPaymentDetailComponent,
    resolve: {
      fixedDoctorPayment: FixedDoctorPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.fixedDoctorPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FixedDoctorPaymentUpdateComponent,
    resolve: {
      fixedDoctorPayment: FixedDoctorPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.fixedDoctorPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FixedDoctorPaymentUpdateComponent,
    resolve: {
      fixedDoctorPayment: FixedDoctorPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.fixedDoctorPayment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const fixedDoctorPaymentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FixedDoctorPaymentDeletePopupComponent,
    resolve: {
      fixedDoctorPayment: FixedDoctorPaymentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.fixedDoctorPayment.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
