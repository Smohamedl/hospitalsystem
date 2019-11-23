import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from './medical-service.service';
import { MedicalServiceComponent } from './medical-service.component';
import { MedicalServiceDetailComponent } from './medical-service-detail.component';
import { MedicalServiceUpdateComponent } from './medical-service-update.component';
import { MedicalServiceDeletePopupComponent } from './medical-service-delete-dialog.component';
import { IMedicalService } from 'app/shared/model/medical-service.model';

@Injectable({ providedIn: 'root' })
export class MedicalServiceResolve implements Resolve<IMedicalService> {
  constructor(private service: MedicalServiceService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedicalService> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((medicalService: HttpResponse<MedicalService>) => medicalService.body));
    }
    return of(new MedicalService());
  }
}

export const medicalServiceRoute: Routes = [
  {
    path: '',
    component: MedicalServiceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.medicalService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MedicalServiceDetailComponent,
    resolve: {
      medicalService: MedicalServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.medicalService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MedicalServiceUpdateComponent,
    resolve: {
      medicalService: MedicalServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.medicalService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MedicalServiceUpdateComponent,
    resolve: {
      medicalService: MedicalServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.medicalService.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const medicalServicePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MedicalServiceDeletePopupComponent,
    resolve: {
      medicalService: MedicalServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.medicalService.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
