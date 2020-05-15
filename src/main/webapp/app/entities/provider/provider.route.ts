import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Provider } from 'app/shared/model/provider.model';
import { ProviderService } from './provider.service';
import { ProviderComponent } from './provider.component';
import { ProviderDetailComponent } from './provider-detail.component';
import { ProviderUpdateComponent } from './provider-update.component';
import { ProviderDeletePopupComponent } from './provider-delete-dialog.component';
import { IProvider } from 'app/shared/model/provider.model';

@Injectable({ providedIn: 'root' })
export class ProvidedrResolve implements Resolve<IProvider> {
  constructor(private service: ProviderService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProvider> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((providedr: HttpResponse<Provider>) => providedr.body));
    }
    return of(new Provider());
  }
}

export const providerRoute: Routes = [
  {
    path: '',
    component: ProviderComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.providedr.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProviderDetailComponent,
    resolve: {
      providedr: ProvidedrResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.providedr.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProviderUpdateComponent,
    resolve: {
      providedr: ProvidedrResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.providedr.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProviderUpdateComponent,
    resolve: {
      providedr: ProvidedrResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.providedr.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const providedrPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProviderDeletePopupComponent,
    resolve: {
      providedr: ProvidedrResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.providedr.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
