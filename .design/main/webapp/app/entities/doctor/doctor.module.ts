import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { DoctorComponent } from './doctor.component';
import { DoctorDetailComponent } from './doctor-detail.component';
import { DoctorUpdateComponent } from './doctor-update.component';
import { DoctorDeletePopupComponent, DoctorDeleteDialogComponent } from './doctor-delete-dialog.component';
import { doctorRoute, doctorPopupRoute } from './doctor.route';

const ENTITY_STATES = [...doctorRoute, ...doctorPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [DoctorComponent, DoctorDetailComponent, DoctorUpdateComponent, DoctorDeleteDialogComponent, DoctorDeletePopupComponent],
  entryComponents: [DoctorDeleteDialogComponent]
})
export class HospitalsystemDoctorModule {}
