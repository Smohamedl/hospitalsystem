import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedicalService } from 'app/shared/model/medical-service.model';

@Component({
  selector: 'jhi-medical-service-detail',
  templateUrl: './medical-service-detail.component.html'
})
export class MedicalServiceDetailComponent implements OnInit {
  medicalService: IMedicalService;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ medicalService }) => {
      this.medicalService = medicalService;
    });
  }

  previousState() {
    window.history.back();
  }
}
