import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISocialOrganization } from 'app/shared/model/social-organization.model';

@Component({
  selector: 'jhi-social-organization-detail',
  templateUrl: './social-organization-detail.component.html'
})
export class SocialOrganizationDetailComponent implements OnInit {
  socialOrganization: ISocialOrganization;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
protected activatedRoute: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ socialOrganization }) => {
      this.socialOrganization = socialOrganization;
    });
  }

  previousState() {
    window.history.back();
  }
}
