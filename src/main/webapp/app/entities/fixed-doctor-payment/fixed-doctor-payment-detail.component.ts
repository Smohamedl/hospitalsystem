import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';

@Component({
  selector: 'jhi-fixed-doctor-payment-detail',
  templateUrl: './fixed-doctor-payment-detail.component.html'
})
export class FixedDoctorPaymentDetailComponent implements OnInit {
  fixedDoctorPayment: IFixedDoctorPayment;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ fixedDoctorPayment }) => {
      this.fixedDoctorPayment = fixedDoctorPayment;
    });
  }

  previousState() {
    window.history.back();
  }
}
