import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { GuardScheduleComponent } from './guard-schedule.component';
import { GuardScheduleDetailComponent } from './guard-schedule-detail.component';
import { GuardScheduleUpdateComponent } from './guard-schedule-update.component';
import { GuardScheduleDeletePopupComponent, GuardScheduleDeleteDialogComponent } from './guard-schedule-delete-dialog.component';
import { guardScheduleRoute, guardSchedulePopupRoute } from './guard-schedule.route';

const ENTITY_STATES = [...guardScheduleRoute, ...guardSchedulePopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GuardScheduleComponent,
    GuardScheduleDetailComponent,
    GuardScheduleUpdateComponent,
    GuardScheduleDeleteDialogComponent,
    GuardScheduleDeletePopupComponent
  ],
  entryComponents: [GuardScheduleDeleteDialogComponent]
})
export class HospitalsystemGuardScheduleModule {}
