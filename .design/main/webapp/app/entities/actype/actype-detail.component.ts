import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActype } from 'app/shared/model/actype.model';

@Component({
  selector: 'jhi-actype-detail',
  templateUrl: './actype-detail.component.html'
})
export class ActypeDetailComponent implements OnInit {
  actype: IActype;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
protected activatedRoute: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ actype }) => {
      this.actype = actype;
    });
  }

  previousState() {
    window.history.back();
  }
}
