import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { flatMap } from 'rxjs/operators';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { ActivateService } from './activate.service';

@Component({
  selector: 'jhi-activate',
  templateUrl: './activate.component.html'
})
export class ActivateComponent implements OnInit {
  error: string;
  success: string;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
private activateService: ActivateService, private loginModalService: LoginModalService, private route: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.route.queryParams.pipe(flatMap(params => this.activateService.get(params.key))).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  login() {
    this.loginModalService.open();
  }
}
