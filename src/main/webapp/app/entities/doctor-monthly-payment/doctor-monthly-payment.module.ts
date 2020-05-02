import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { DoctorMonthlyPaymentComponent } from './doctor-monthly-payment.component';
import { DoctorMonthlyPaymentDetailComponent } from './doctor-monthly-payment-detail.component';
import { DoctorMonthlyPaymentUpdateComponent } from './doctor-monthly-payment-update.component';
import {
  DoctorMonthlyPaymentDeletePopupComponent,
  DoctorMonthlyPaymentDeleteDialogComponent
} from './doctor-monthly-payment-delete-dialog.component';
import { doctorMonthlyPaymentRoute, doctorMonthlyPaymentPopupRoute } from './doctor-monthly-payment.route';

const ENTITY_STATES = [...doctorMonthlyPaymentRoute, ...doctorMonthlyPaymentPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DoctorMonthlyPaymentComponent,
    DoctorMonthlyPaymentDetailComponent,
    DoctorMonthlyPaymentUpdateComponent,
    DoctorMonthlyPaymentDeleteDialogComponent,
    DoctorMonthlyPaymentDeletePopupComponent
  ],
  entryComponents: [DoctorMonthlyPaymentDeleteDialogComponent]
})
export class HospitalsystemDoctorMonthlyPaymentModule {}
