import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Guard } from 'app/shared/model/guard.model';
import { GuardService } from './guard.service';
import { GuardComponent } from './guard.component';
import { GuardDetailComponent } from './guard-detail.component';
import { GuardUpdateComponent } from './guard-update.component';
import { GuardDeletePopupComponent } from './guard-delete-dialog.component';
import { IGuard } from 'app/shared/model/guard.model';

@Injectable({ providedIn: 'root' })
export class GuardResolve implements Resolve<IGuard> {
  constructor(private service: GuardService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuard> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((guard: HttpResponse<Guard>) => guard.body));
    }
    return of(new Guard());
  }
}

export const guardRoute: Routes = [
  {
    path: '',
    component: GuardComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.guard.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GuardDetailComponent,
    resolve: {
      guard: GuardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guard.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GuardUpdateComponent,
    resolve: {
      guard: GuardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guard.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GuardUpdateComponent,
    resolve: {
      guard: GuardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guard.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const guardPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GuardDeletePopupComponent,
    resolve: {
      guard: GuardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guard.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
