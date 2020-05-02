import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { User } from 'app/core/user/user.model';

@Component({
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html'
})
export class UserManagementDetailComponent implements OnInit {
  user: User;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
private route: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.route.data.subscribe(({ user }) => {
      this.user = user.body ? user.body : user;
    });
  }
}
