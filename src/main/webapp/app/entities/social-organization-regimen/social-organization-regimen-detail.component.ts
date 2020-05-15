import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

@Component({
  selector: 'jhi-social-organization-regimen-detail',
  templateUrl: './social-organization-regimen-detail.component.html'
})
export class SocialOrganizationRegimenDetailComponent implements OnInit {
  socialOrganizationRegimen: ISocialOrganizationRegimen;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ socialOrganizationRegimen }) => {
      this.socialOrganizationRegimen = socialOrganizationRegimen;
    });
  }

  previousState() {
    window.history.back();
  }
}
