import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Hospitalization } from 'app/shared/model/hospitalization.model';
import { HospitalizationService } from './hospitalization.service';
import { HospitalizationComponent } from './hospitalization.component';
import { HospitalizationDetailComponent } from './hospitalization-detail.component';
import { HospitalizationUpdateComponent } from './hospitalization-update.component';
import { HospitalizationDeletePopupComponent } from './hospitalization-delete-dialog.component';
import { IHospitalization } from 'app/shared/model/hospitalization.model';

@Injectable({ providedIn: 'root' })
export class HospitalizationResolve implements Resolve<IHospitalization> {
  constructor(private service: HospitalizationService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHospitalization> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((hospitalization: HttpResponse<Hospitalization>) => hospitalization.body));
    }
    return of(new Hospitalization());
  }
}

export const hospitalizationRoute: Routes = [
  {
    path: '',
    component: HospitalizationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.hospitalization.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HospitalizationDetailComponent,
    resolve: {
      hospitalization: HospitalizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.hospitalization.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HospitalizationUpdateComponent,
    resolve: {
      hospitalization: HospitalizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.hospitalization.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HospitalizationUpdateComponent,
    resolve: {
      hospitalization: HospitalizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.hospitalization.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const hospitalizationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: HospitalizationDeletePopupComponent,
    resolve: {
      hospitalization: HospitalizationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.hospitalization.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
