import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { PatientComponent } from './patient.component';
import { PatientDetailComponent } from './patient-detail.component';
import { PatientUpdateComponent } from './patient-update.component';
import { PatientDeletePopupComponent, PatientDeleteDialogComponent } from './patient-delete-dialog.component';
import { patientRoute, patientPopupRoute } from './patient.route';

const ENTITY_STATES = [...patientRoute, ...patientPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PatientComponent,
    PatientDetailComponent,
    PatientUpdateComponent,
    PatientDeleteDialogComponent,
    PatientDeletePopupComponent
  ],
  entryComponents: [PatientDeleteDialogComponent]
})
export class HospitalsystemPatientModule {}
