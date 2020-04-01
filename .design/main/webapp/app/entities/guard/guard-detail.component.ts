import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuard } from 'app/shared/model/guard.model';

@Component({
  selector: 'jhi-guard-detail',
  templateUrl: './guard-detail.component.html'
})
export class GuardDetailComponent implements OnInit {
  guard: IGuard;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
protected activatedRoute: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ guard }) => {
      this.guard = guard;
    });
  }

  previousState() {
    window.history.back();
  }
}
