import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';

@Component({
  selector: 'jhi-guard-schedule-detail',
  templateUrl: './guard-schedule-detail.component.html'
})
export class GuardScheduleDetailComponent implements OnInit {
  guardSchedule: IGuardSchedule;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
protected activatedRoute: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ guardSchedule }) => {
      this.guardSchedule = guardSchedule;
    });
  }

  previousState() {
    window.history.back();
  }
}
