import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Actype } from 'app/shared/model/actype.model';
import { ActypeService } from './actype.service';
import { ActypeComponent } from './actype.component';
import { ActypeDetailComponent } from './actype-detail.component';
import { ActypeUpdateComponent } from './actype-update.component';
import { ActypeDeletePopupComponent } from './actype-delete-dialog.component';
import { IActype } from 'app/shared/model/actype.model';

@Injectable({ providedIn: 'root' })
export class ActypeResolve implements Resolve<IActype> {
  constructor(private service: ActypeService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActype> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((actype: HttpResponse<Actype>) => actype.body));
    }
    return of(new Actype());
  }
}

export const actypeRoute: Routes = [
  {
    path: '',
    component: ActypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.actype.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ActypeDetailComponent,
    resolve: {
      actype: ActypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.actype.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ActypeUpdateComponent,
    resolve: {
      actype: ActypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.actype.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ActypeUpdateComponent,
    resolve: {
      actype: ActypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.actype.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const actypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ActypeDeletePopupComponent,
    resolve: {
      actype: ActypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.actype.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
