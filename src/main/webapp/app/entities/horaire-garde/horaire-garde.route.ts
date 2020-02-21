import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Horaire_garde } from 'app/shared/model/horaire-garde.model';
import { Horaire_gardeService } from './horaire-garde.service';
import { Horaire_gardeComponent } from './horaire-garde.component';
import { Horaire_gardeDetailComponent } from './horaire-garde-detail.component';
import { Horaire_gardeUpdateComponent } from './horaire-garde-update.component';
import { Horaire_gardeDeletePopupComponent } from './horaire-garde-delete-dialog.component';
import { IHoraire_garde } from 'app/shared/model/horaire-garde.model';

@Injectable({ providedIn: 'root' })
export class Horaire_gardeResolve implements Resolve<IHoraire_garde> {
  constructor(private service: Horaire_gardeService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHoraire_garde> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((horaire_garde: HttpResponse<Horaire_garde>) => horaire_garde.body));
    }
    return of(new Horaire_garde());
  }
}

export const horaire_gardeRoute: Routes = [
  {
    path: '',
    component: Horaire_gardeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.horaire_garde.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: Horaire_gardeDetailComponent,
    resolve: {
      horaire_garde: Horaire_gardeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.horaire_garde.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: Horaire_gardeUpdateComponent,
    resolve: {
      horaire_garde: Horaire_gardeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.horaire_garde.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: Horaire_gardeUpdateComponent,
    resolve: {
      horaire_garde: Horaire_gardeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.horaire_garde.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const horaire_gardePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: Horaire_gardeDeletePopupComponent,
    resolve: {
      horaire_garde: Horaire_gardeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.horaire_garde.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
