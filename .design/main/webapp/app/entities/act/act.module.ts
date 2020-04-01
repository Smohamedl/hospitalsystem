import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { ActComponent } from './act.component';
import { ActDetailComponent } from './act-detail.component';
import { ActUpdateComponent } from './act-update.component';
import { ActDeletePopupComponent, ActDeleteDialogComponent } from './act-delete-dialog.component';
import { actRoute, actPopupRoute } from './act.route';

const ENTITY_STATES = [...actRoute, ...actPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ActComponent, ActDetailComponent, ActUpdateComponent, ActDeleteDialogComponent, ActDeletePopupComponent],
  entryComponents: [ActDeleteDialogComponent]
})
export class HospitalsystemActModule {}
