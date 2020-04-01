import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReceiptAct } from 'app/shared/model/receipt-act.model';
import { ReceiptActService } from './receipt-act.service';
import { ReceiptActComponent } from './receipt-act.component';
import { ReceiptActDetailComponent } from './receipt-act-detail.component';
import { ReceiptActUpdateComponent } from './receipt-act-update.component';
import { ReceiptActDeletePopupComponent } from './receipt-act-delete-dialog.component';
import { IReceiptAct } from 'app/shared/model/receipt-act.model';

@Injectable({ providedIn: 'root' })
export class ReceiptActResolve implements Resolve<IReceiptAct> {
  constructor(private service: ReceiptActService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReceiptAct> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((receiptAct: HttpResponse<ReceiptAct>) => receiptAct.body));
    }
    return of(new ReceiptAct());
  }
}

export const receiptActRoute: Routes = [
  {
    path: '',
    component: ReceiptActComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.receiptAct.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReceiptActDetailComponent,
    resolve: {
      receiptAct: ReceiptActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.receiptAct.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReceiptActUpdateComponent,
    resolve: {
      receiptAct: ReceiptActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.receiptAct.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReceiptActUpdateComponent,
    resolve: {
      receiptAct: ReceiptActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.receiptAct.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const receiptActPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ReceiptActDeletePopupComponent,
    resolve: {
      receiptAct: ReceiptActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.receiptAct.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
