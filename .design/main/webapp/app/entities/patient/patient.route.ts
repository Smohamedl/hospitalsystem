import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { PatientComponent } from './patient.component';
import { PatientDetailComponent } from './patient-detail.component';
import { PatientUpdateComponent } from './patient-update.component';
import { PatientDeletePopupComponent } from './patient-delete-dialog.component';
import { IPatient } from 'app/shared/model/patient.model';

@Injectable({ providedIn: 'root' })
export class PatientResolve implements Resolve<IPatient> {
  constructor(private service: PatientService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatient> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((patient: HttpResponse<Patient>) => patient.body));
    }
    return of(new Patient());
  }
}

export const patientRoute: Routes = [
  {
    path: '',
    component: PatientComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'hospitalsystemApp.patient.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PatientDetailComponent,
    resolve: {
      patient: PatientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.patient.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PatientUpdateComponent,
    resolve: {
      patient: PatientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.patient.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PatientUpdateComponent,
    resolve: {
      patient: PatientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.patient.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const patientPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PatientDeletePopupComponent,
    resolve: {
      patient: PatientResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.patient.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
