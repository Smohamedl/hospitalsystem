import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { QuantityPriceComponent } from './quantity-price.component';
import { QuantityPriceDetailComponent } from './quantity-price-detail.component';
import { QuantityPriceUpdateComponent } from './quantity-price-update.component';
import { QuantityPriceDeletePopupComponent, QuantityPriceDeleteDialogComponent } from './quantity-price-delete-dialog.component';
import { quantityPriceRoute, quantityPricePopupRoute } from './quantity-price.route';

const ENTITY_STATES = [...quantityPriceRoute, ...quantityPricePopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    QuantityPriceComponent,
    QuantityPriceDetailComponent,
    QuantityPriceUpdateComponent,
    QuantityPriceDeleteDialogComponent,
    QuantityPriceDeletePopupComponent
  ],
  entryComponents: [QuantityPriceDeleteDialogComponent]
})
export class HospitalsystemQuantityPriceModule {}
