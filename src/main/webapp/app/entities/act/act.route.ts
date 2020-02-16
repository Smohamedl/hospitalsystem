import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Act } from 'app/shared/model/act.model';
import { ActService } from './act.service';
import { ActComponent } from './act.component';
import { ActDetailComponent } from './act-detail.component';
import { ActUpdateComponent } from './act-update.component';
import { ActDeletePopupComponent } from './act-delete-dialog.component';
import { IAct } from 'app/shared/model/act.model';

@Injectable({ providedIn: 'root' })
export class ActResolve implements Resolve<IAct> {
  constructor(private service: ActService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAct> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((act: HttpResponse<Act>) => act.body));
    }
    return of(new Act());
  }
}

export const actRoute: Routes = [
  {
    path: '',
    component: ActComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.act.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ActDetailComponent,
    resolve: {
      act: ActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.act.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ActUpdateComponent,
    resolve: {
      act: ActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.act.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ActUpdateComponent,
    resolve: {
      act: ActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.act.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const actPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ActDeletePopupComponent,
    resolve: {
      act: ActResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.act.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
