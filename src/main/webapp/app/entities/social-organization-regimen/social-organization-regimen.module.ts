import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { SocialOrganizationRegimenComponent } from './social-organization-regimen.component';
import { SocialOrganizationRegimenDetailComponent } from './social-organization-regimen-detail.component';
import { SocialOrganizationRegimenUpdateComponent } from './social-organization-regimen-update.component';
import {
  SocialOrganizationRegimenDeletePopupComponent,
  SocialOrganizationRegimenDeleteDialogComponent
} from './social-organization-regimen-delete-dialog.component';
import { socialOrganizationRegimenRoute, socialOrganizationRegimenPopupRoute } from './social-organization-regimen.route';

const ENTITY_STATES = [...socialOrganizationRegimenRoute, ...socialOrganizationRegimenPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SocialOrganizationRegimenComponent,
    SocialOrganizationRegimenDetailComponent,
    SocialOrganizationRegimenUpdateComponent,
    SocialOrganizationRegimenDeleteDialogComponent,
    SocialOrganizationRegimenDeletePopupComponent
  ],
  entryComponents: [SocialOrganizationRegimenDeleteDialogComponent]
})
export class HospitalsystemSocialOrganizationRegimenModule {}
