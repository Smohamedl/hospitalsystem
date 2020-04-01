import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { MedicalServiceComponent } from './medical-service.component';
import { MedicalServiceDetailComponent } from './medical-service-detail.component';
import { MedicalServiceUpdateComponent } from './medical-service-update.component';
import { MedicalServiceDeletePopupComponent, MedicalServiceDeleteDialogComponent } from './medical-service-delete-dialog.component';
import { medicalServiceRoute, medicalServicePopupRoute } from './medical-service.route';

const ENTITY_STATES = [...medicalServiceRoute, ...medicalServicePopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MedicalServiceComponent,
    MedicalServiceDetailComponent,
    MedicalServiceUpdateComponent,
    MedicalServiceDeleteDialogComponent,
    MedicalServiceDeletePopupComponent
  ],
  entryComponents: [MedicalServiceDeleteDialogComponent]
})
export class HospitalsystemMedicalServiceModule {}
