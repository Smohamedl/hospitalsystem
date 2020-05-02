import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { DoctorPartPaymentComponent } from './doctor-part-payment.component';
import { DoctorPartPaymentDetailComponent } from './doctor-part-payment-detail.component';
import { DoctorPartPaymentUpdateComponent } from './doctor-part-payment-update.component';
import {
  DoctorPartPaymentDeletePopupComponent,
  DoctorPartPaymentDeleteDialogComponent
} from './doctor-part-payment-delete-dialog.component';
import { doctorPartPaymentRoute, doctorPartPaymentPopupRoute } from './doctor-part-payment.route';

const ENTITY_STATES = [...doctorPartPaymentRoute, ...doctorPartPaymentPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DoctorPartPaymentComponent,
    DoctorPartPaymentDetailComponent,
    DoctorPartPaymentUpdateComponent,
    DoctorPartPaymentDeleteDialogComponent,
    DoctorPartPaymentDeletePopupComponent
  ],
  entryComponents: [DoctorPartPaymentDeleteDialogComponent]
})
export class HospitalsystemDoctorPartPaymentModule {}
