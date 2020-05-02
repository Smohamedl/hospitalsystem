import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-alert',
  template: `
    <div class="alerts" role="alert">
      <div *ngFor="let alert of alerts" [ngClass]="setClasses(alert)">
        <ngb-alert *ngIf="alert && alert.type && alert.msg" [type]="alert.type" (close)="alert.close(alerts)">
          <pre [innerHTML]="alert.msg"></pre>
        </ngb-alert>
      </div>
    </div>
  `
})
export class JhiAlertComponent implements OnInit, OnDestroy {
  alerts: any[];

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
private alertService: JhiAlertService) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.alerts = this.alertService.get();
  }

  setClasses(alert) {
    return {
      'jhi-toast': alert.toast,
      [alert.position]: true
    };
  }

  ngOnDestroy() {
    this.alerts = [];
  }
}
