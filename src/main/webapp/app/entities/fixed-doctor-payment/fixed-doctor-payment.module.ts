import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { FixedDoctorPaymentComponent } from './fixed-doctor-payment.component';
import { FixedDoctorPaymentDetailComponent } from './fixed-doctor-payment-detail.component';
import { FixedDoctorPaymentUpdateComponent } from './fixed-doctor-payment-update.component';
import {
  FixedDoctorPaymentDeletePopupComponent,
  FixedDoctorPaymentDeleteDialogComponent
} from './fixed-doctor-payment-delete-dialog.component';
import { fixedDoctorPaymentRoute, fixedDoctorPaymentPopupRoute } from './fixed-doctor-payment.route';

const ENTITY_STATES = [...fixedDoctorPaymentRoute, ...fixedDoctorPaymentPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FixedDoctorPaymentComponent,
    FixedDoctorPaymentDetailComponent,
    FixedDoctorPaymentUpdateComponent,
    FixedDoctorPaymentDeleteDialogComponent,
    FixedDoctorPaymentDeletePopupComponent
  ],
  entryComponents: [FixedDoctorPaymentDeleteDialogComponent]
})
export class HospitalsystemFixedDoctorPaymentModule {}
