import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { ActComponent } from './act.component';
import { ActDetailComponent } from './act-detail.component';
import { ActUpdateComponent } from './act-update.component';
import { ActDeletePopupComponent, ActDeleteDialogComponent } from './act-delete-dialog.component';
import { actRoute, actPopupRoute } from './act.route';
import {MatAutocompleteModule,MatInputModule} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AutoCompleteModule} from 'primeng/autocomplete';

const ENTITY_STATES = [...actRoute, ...actPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES),
      MatAutocompleteModule,
      MatInputModule,
      FormsModule,
      ReactiveFormsModule,
      AutoCompleteModule],
  declarations: [ActComponent, ActDetailComponent, ActUpdateComponent, ActDeleteDialogComponent, ActDeletePopupComponent],
  entryComponents: [ActDeleteDialogComponent]
})
export class HospitalsystemActModule {}
