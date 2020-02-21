import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { Horaire_gardeComponent } from './horaire-garde.component';
import { Horaire_gardeDetailComponent } from './horaire-garde-detail.component';
import { Horaire_gardeUpdateComponent } from './horaire-garde-update.component';
import { Horaire_gardeDeletePopupComponent, Horaire_gardeDeleteDialogComponent } from './horaire-garde-delete-dialog.component';
import { horaire_gardeRoute, horaire_gardePopupRoute } from './horaire-garde.route';

const ENTITY_STATES = [...horaire_gardeRoute, ...horaire_gardePopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    Horaire_gardeComponent,
    Horaire_gardeDetailComponent,
    Horaire_gardeUpdateComponent,
    Horaire_gardeDeleteDialogComponent,
    Horaire_gardeDeletePopupComponent
  ],
  entryComponents: [Horaire_gardeDeleteDialogComponent]
})
export class HospitalsystemHoraire_gardeModule {}
