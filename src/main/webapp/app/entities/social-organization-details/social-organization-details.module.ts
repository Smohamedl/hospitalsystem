import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { SocialOrganizationDetailsComponent } from './social-organization-details.component';
import { SocialOrganizationDetailsDetailComponent } from './social-organization-details-detail.component';
import { SocialOrganizationDetailsUpdateComponent } from './social-organization-details-update.component';
import {
  SocialOrganizationDetailsDeletePopupComponent,
  SocialOrganizationDetailsDeleteDialogComponent
} from './social-organization-details-delete-dialog.component';
import { socialOrganizationDetailsRoute, socialOrganizationDetailsPopupRoute } from './social-organization-details.route';

const ENTITY_STATES = [...socialOrganizationDetailsRoute, ...socialOrganizationDetailsPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SocialOrganizationDetailsComponent,
    SocialOrganizationDetailsDetailComponent,
    SocialOrganizationDetailsUpdateComponent,
    SocialOrganizationDetailsDeleteDialogComponent,
    SocialOrganizationDetailsDeletePopupComponent
  ],
  entryComponents: [SocialOrganizationDetailsDeleteDialogComponent]
})
export class HospitalsystemSocialOrganizationDetailsModule {}
