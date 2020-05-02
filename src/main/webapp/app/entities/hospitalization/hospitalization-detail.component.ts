import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHospitalization } from 'app/shared/model/hospitalization.model';

@Component({
  selector: 'jhi-hospitalization-detail',
  templateUrl: './hospitalization-detail.component.html'
})
export class HospitalizationDetailComponent implements OnInit {
  hospitalization: IHospitalization;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ hospitalization }) => {
      this.hospitalization = hospitalization;
    });
  }

  previousState() {
    window.history.back();
  }
}
