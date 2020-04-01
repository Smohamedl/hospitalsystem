import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { SessionComponent } from './session.component';
import { SessionDetailComponent } from './session-detail.component';
import { sessionRoute, sessionPopupRoute } from './session.route';

const ENTITY_STATES = [...sessionRoute, ...sessionPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [SessionComponent, SessionDetailComponent],
  entryComponents: []
})
export class HospitalsystemSessionModule {}
