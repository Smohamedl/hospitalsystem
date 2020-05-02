import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GuardSchedule } from 'app/shared/model/guard-schedule.model';
import { GuardScheduleService } from './guard-schedule.service';
import { GuardScheduleComponent } from './guard-schedule.component';
import { GuardScheduleDetailComponent } from './guard-schedule-detail.component';
import { GuardScheduleUpdateComponent } from './guard-schedule-update.component';
import { GuardScheduleDeletePopupComponent } from './guard-schedule-delete-dialog.component';
import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';

@Injectable({ providedIn: 'root' })
export class GuardScheduleResolve implements Resolve<IGuardSchedule> {
  constructor(private service: GuardScheduleService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuardSchedule> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((guardSchedule: HttpResponse<GuardSchedule>) => guardSchedule.body));
    }
    return of(new GuardSchedule());
  }
}

export const guardScheduleRoute: Routes = [
  {
    path: '',
    component: GuardScheduleComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guardSchedule.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GuardScheduleDetailComponent,
    resolve: {
      guardSchedule: GuardScheduleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guardSchedule.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GuardScheduleUpdateComponent,
    resolve: {
      guardSchedule: GuardScheduleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guardSchedule.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GuardScheduleUpdateComponent,
    resolve: {
      guardSchedule: GuardScheduleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guardSchedule.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const guardSchedulePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GuardScheduleDeletePopupComponent,
    resolve: {
      guardSchedule: GuardScheduleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hospitalsystemApp.guardSchedule.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
