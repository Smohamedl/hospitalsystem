import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { SocialOrganizationComponent } from './social-organization.component';
import { SocialOrganizationDetailComponent } from './social-organization-detail.component';
import { SocialOrganizationUpdateComponent } from './social-organization-update.component';
import {
  SocialOrganizationDeletePopupComponent,
  SocialOrganizationDeleteDialogComponent
} from './social-organization-delete-dialog.component';
import { socialOrganizationRoute, socialOrganizationPopupRoute } from './social-organization.route';

const ENTITY_STATES = [...socialOrganizationRoute, ...socialOrganizationPopupRoute];

@NgModule({
  imports: [HospitalsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SocialOrganizationComponent,
    SocialOrganizationDetailComponent,
    SocialOrganizationUpdateComponent,
    SocialOrganizationDeleteDialogComponent,
    SocialOrganizationDeletePopupComponent
  ],
  entryComponents: [SocialOrganizationDeleteDialogComponent]
})
export class HospitalsystemSocialOrganizationModule {}
