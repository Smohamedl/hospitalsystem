import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { ProviderComponent } from './provider.component';
import { ProviderDetailComponent } from './provider-detail.component';
import { ProviderUpdateComponent } from './provider-update.component';
import { ProvidedrDeletePopupComponent, ProviderDeleteDialogComponent } from './provider-delete-dialog.component';
import { providerRoute, providedrPopupRoute } from './provider.route';

const ENTITY_STATES = [...providerRoute, ...providedrPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProviderComponent,
    ProviderDetailComponent,
    ProviderUpdateComponent,
    ProviderDeleteDialogComponent,
    ProvidedrDeletePopupComponent
  ],
  entryComponents: [ProviderDeleteDialogComponent]
})
export class HospitalsystemProvidedrModule {}
