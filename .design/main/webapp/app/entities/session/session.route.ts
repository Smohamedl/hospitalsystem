import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Session } from 'app/shared/model/session.model';
import { SessionService } from './session.service';
import { SessionComponent } from './session.component';
import { SessionDetailComponent } from './session-detail.component';
import { ISession } from 'app/shared/model/session.model';

@Injectable({ providedIn: 'root' })
export class SessionResolve implements Resolve<ISession> {
  constructor(private service: SessionService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISession> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((session: HttpResponse<Session>) => session.body));
    }
    return of(new Session());
  }
}

export const sessionRoute: Routes = [
  {
    path: '',
    component: SessionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.session.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SessionDetailComponent,
    resolve: {
      session: SessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.session.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const sessionPopupRoute: Routes = [];
