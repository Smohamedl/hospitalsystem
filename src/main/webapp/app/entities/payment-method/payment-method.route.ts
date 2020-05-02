import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { PaymentMethodComponent } from './payment-method.component';
import { PaymentMethodDetailComponent } from './payment-method-detail.component';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';

@Injectable({ providedIn: 'root' })
export class PaymentMethodResolve implements Resolve<IPaymentMethod> {
  constructor(private service: PaymentMethodService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentMethod> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((paymentMethod: HttpResponse<PaymentMethod>) => paymentMethod.body));
    }
    return of(new PaymentMethod());
  }
}

export const paymentMethodRoute: Routes = [
  {
    path: '',
    component: PaymentMethodComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.paymentMethod.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PaymentMethodDetailComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.paymentMethod.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const paymentMethodPopupRoute: Routes = [];
