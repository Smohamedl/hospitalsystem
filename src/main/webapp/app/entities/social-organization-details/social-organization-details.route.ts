import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';
import { SocialOrganizationDetailsService } from './social-organization-details.service';
import { SocialOrganizationDetailsComponent } from './social-organization-details.component';
import { SocialOrganizationDetailsDetailComponent } from './social-organization-details-detail.component';
import { SocialOrganizationDetailsUpdateComponent } from './social-organization-details-update.component';
import { SocialOrganizationDetailsDeletePopupComponent } from './social-organization-details-delete-dialog.component';
import { ISocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';

@Injectable({ providedIn: 'root' })
export class SocialOrganizationDetailsResolve implements Resolve<ISocialOrganizationDetails> {
  constructor(private service: SocialOrganizationDetailsService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISocialOrganizationDetails> {
    const id = route.params['id'];
    if (id) {
      return this.service
        .find(id)
        .pipe(map((socialOrganizationDetails: HttpResponse<SocialOrganizationDetails>) => socialOrganizationDetails.body));
    }
    return of(new SocialOrganizationDetails());
  }
}

export const socialOrganizationDetailsRoute: Routes = [
  {
    path: '',
    component: SocialOrganizationDetailsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.socialOrganizationDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SocialOrganizationDetailsDetailComponent,
    resolve: {
      socialOrganizationDetails: SocialOrganizationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SocialOrganizationDetailsUpdateComponent,
    resolve: {
      socialOrganizationDetails: SocialOrganizationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SocialOrganizationDetailsUpdateComponent,
    resolve: {
      socialOrganizationDetails: SocialOrganizationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const socialOrganizationDetailsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SocialOrganizationDetailsDeletePopupComponent,
    resolve: {
      socialOrganizationDetails: SocialOrganizationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.socialOrganizationDetails.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
