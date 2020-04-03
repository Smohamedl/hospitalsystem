import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';

@Component({
  selector: 'jhi-social-organization-details-detail',
  templateUrl: './social-organization-details-detail.component.html'
})
export class SocialOrganizationDetailsDetailComponent implements OnInit {
  socialOrganizationDetails: ISocialOrganizationDetails;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ socialOrganizationDetails }) => {
      this.socialOrganizationDetails = socialOrganizationDetails;
    });
  }

  previousState() {
    window.history.back();
  }
}
