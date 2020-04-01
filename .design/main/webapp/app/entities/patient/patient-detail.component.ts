import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatient } from 'app/shared/model/patient.model';

@Component({
  selector: 'jhi-patient-detail',
  templateUrl: './patient-detail.component.html'
})
export class PatientDetailComponent implements OnInit {
  patient: IPatient;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
protected activatedRoute: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.patient = patient;
    });
  }

  previousState() {
    window.history.back();
  }
}
