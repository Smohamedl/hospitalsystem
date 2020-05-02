import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';
import { SocialOrganizationRegimenService } from './social-organization-regimen.service';
import { SocialOrganizationRegimenComponent } from './social-organization-regimen.component';
import { SocialOrganizationRegimenDetailComponent } from './social-organization-regimen-detail.component';
import { SocialOrganizationRegimenUpdateComponent } from './social-organization-regimen-update.component';
import { SocialOrganizationRegimenDeletePopupComponent } from './social-organization-regimen-delete-dialog.component';
import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

@Injectable({ providedIn: 'root' })
export class SocialOrganizationRegimenResolve implements Resolve<ISocialOrganizationRegimen> {
  constructor(private service: SocialOrganizationRegimenService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISocialOrganizationRegimen> {
    const id = route.params['id'];
    if (id) {
      return this.service
        .find(id)
        .pipe(map((socialOrganizationRegimen: HttpResponse<SocialOrganizationRegimen>) => socialOrganizationRegimen.body));
    }
    return of(new SocialOrganizationRegimen());
  }
}

export const socialOrganizationRegimenRoute: Routes = [
  {
    path: '',
    component: SocialOrganizationRegimenComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.socialOrganizationRegimen.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SocialOrganizationRegimenDetailComponent,
    resolve: {
      socialOrganizationRegimen: SocialOrganizationRegimenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationRegimen.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SocialOrganizationRegimenUpdateComponent,
    resolve: {
      socialOrganizationRegimen: SocialOrganizationRegimenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationRegimen.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SocialOrganizationRegimenUpdateComponent,
    resolve: {
      socialOrganizationRegimen: SocialOrganizationRegimenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationRegimen.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const socialOrganizationRegimenPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SocialOrganizationRegimenDeletePopupComponent,
    resolve: {
      socialOrganizationRegimen: SocialOrganizationRegimenResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationRegimen.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
