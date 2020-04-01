import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { GuardComponent } from './guard.component';
import { GuardDetailComponent } from './guard-detail.component';
import { GuardUpdateComponent } from './guard-update.component';
import { GuardDeletePopupComponent, GuardDeleteDialogComponent } from './guard-delete-dialog.component';
import { guardRoute, guardPopupRoute } from './guard.route';

const ENTITY_STATES = [...guardRoute, ...guardPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [GuardComponent, GuardDetailComponent, GuardUpdateComponent, GuardDeleteDialogComponent, GuardDeletePopupComponent],
  entryComponents: [GuardDeleteDialogComponent]
})
export class HospitalsystemGuardModule {}
