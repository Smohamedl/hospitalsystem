import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SocialOrganization } from 'app/shared/model/social-organization.model';
import { SocialOrganizationService } from './social-organization.service';
import { SocialOrganizationComponent } from './social-organization.component';
import { SocialOrganizationDetailComponent } from './social-organization-detail.component';
import { SocialOrganizationUpdateComponent } from './social-organization-update.component';
import { SocialOrganizationDeletePopupComponent } from './social-organization-delete-dialog.component';
import { ISocialOrganization } from 'app/shared/model/social-organization.model';

@Injectable({ providedIn: 'root' })
export class SocialOrganizationResolve implements Resolve<ISocialOrganization> {
  constructor(private service: SocialOrganizationService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISocialOrganization> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((socialOrganization: HttpResponse<SocialOrganization>) => socialOrganization.body));
    }
    return of(new SocialOrganization());
  }
}

export const socialOrganizationRoute: Routes = [
  {
    path: '',
    component: SocialOrganizationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.socialOrganization.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SocialOrganizationDetailComponent,
    resolve: {
      socialOrganization: SocialOrganizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganization.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SocialOrganizationUpdateComponent,
    resolve: {
      socialOrganization: SocialOrganizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganization.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SocialOrganizationUpdateComponent,
    resolve: {
      socialOrganization: SocialOrganizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganization.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const socialOrganizationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SocialOrganizationDeletePopupComponent,
    resolve: {
      socialOrganization: SocialOrganizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganization.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
