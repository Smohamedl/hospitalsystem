import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { ActypeComponent } from './actype.component';
import { ActypeDetailComponent } from './actype-detail.component';
import { ActypeUpdateComponent } from './actype-update.component';
import { ActypeDeletePopupComponent, ActypeDeleteDialogComponent } from './actype-delete-dialog.component';
import { actypeRoute, actypePopupRoute } from './actype.route';

const ENTITY_STATES = [...actypeRoute, ...actypePopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ActypeComponent, ActypeDetailComponent, ActypeUpdateComponent, ActypeDeleteDialogComponent, ActypeDeletePopupComponent],
  entryComponents: [ActypeDeleteDialogComponent]
})
export class HospitalsystemActypeModule {}
