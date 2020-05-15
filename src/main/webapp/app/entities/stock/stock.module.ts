import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { StockComponent } from './stock.component';
import { StockDetailComponent } from './stock-detail.component';
import { StockUpdateComponent } from './stock-update.component';
import { StockDeletePopupComponent, StockDeleteDialogComponent } from './stock-delete-dialog.component';
import { stockRoute, stockPopupRoute } from './stock.route';

const ENTITY_STATES = [...stockRoute, ...stockPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [StockComponent, StockDetailComponent, StockUpdateComponent, StockDeleteDialogComponent, StockDeletePopupComponent],
  entryComponents: [StockDeleteDialogComponent]
})
export class HospitalsystemStockModule {}
