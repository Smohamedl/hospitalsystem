import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { ReceiptActComponent } from './receipt-act.component';
import { ReceiptActDetailComponent } from './receipt-act-detail.component';
import { ReceiptActUpdateComponent } from './receipt-act-update.component';
import { ReceiptActDeletePopupComponent, ReceiptActDeleteDialogComponent } from './receipt-act-delete-dialog.component';
import { receiptActRoute, receiptActPopupRoute } from './receipt-act.route';

const ENTITY_STATES = [...receiptActRoute, ...receiptActPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ReceiptActComponent,
    ReceiptActDetailComponent,
    ReceiptActUpdateComponent,
    ReceiptActDeleteDialogComponent,
    ReceiptActDeletePopupComponent
  ],
  entryComponents: [ReceiptActDeleteDialogComponent]
})
export class HospitalsystemReceiptActModule {}
