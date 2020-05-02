import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { HospitalizationComponent } from './hospitalization.component';
import { HospitalizationDetailComponent } from './hospitalization-detail.component';
import { HospitalizationUpdateComponent } from './hospitalization-update.component';
import { HospitalizationDeletePopupComponent, HospitalizationDeleteDialogComponent } from './hospitalization-delete-dialog.component';
import { hospitalizationRoute, hospitalizationPopupRoute } from './hospitalization.route';

const ENTITY_STATES = [...hospitalizationRoute, ...hospitalizationPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    HospitalizationComponent,
    HospitalizationDetailComponent,
    HospitalizationUpdateComponent,
    HospitalizationDeleteDialogComponent,
    HospitalizationDeletePopupComponent
  ],
  entryComponents: [HospitalizationDeleteDialogComponent]
})
export class HospitalsystemHospitalizationModule {}
