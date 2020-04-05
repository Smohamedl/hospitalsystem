import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { QuantityPrice } from 'app/shared/model/quantity-price.model';
import { QuantityPriceService } from './quantity-price.service';
import { QuantityPriceComponent } from './quantity-price.component';
import { QuantityPriceDetailComponent } from './quantity-price-detail.component';
import { QuantityPriceUpdateComponent } from './quantity-price-update.component';
import { QuantityPriceDeletePopupComponent } from './quantity-price-delete-dialog.component';
import { IQuantityPrice } from 'app/shared/model/quantity-price.model';

@Injectable({ providedIn: 'root' })
export class QuantityPriceResolve implements Resolve<IQuantityPrice> {
  constructor(private service: QuantityPriceService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuantityPrice> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((quantityPrice: HttpResponse<QuantityPrice>) => quantityPrice.body));
    }
    return of(new QuantityPrice());
  }
}

export const quantityPriceRoute: Routes = [
  {
    path: '',
    component: QuantityPriceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.quantityPrice.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: QuantityPriceDetailComponent,
    resolve: {
      quantityPrice: QuantityPriceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.quantityPrice.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: QuantityPriceUpdateComponent,
    resolve: {
      quantityPrice: QuantityPriceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.quantityPrice.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: QuantityPriceUpdateComponent,
    resolve: {
      quantityPrice: QuantityPriceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.quantityPrice.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const quantityPricePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: QuantityPriceDeletePopupComponent,
    resolve: {
      quantityPrice: QuantityPriceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.quantityPrice.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
