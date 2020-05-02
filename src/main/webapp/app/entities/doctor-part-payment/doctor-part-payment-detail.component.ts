import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';

@Component({
  selector: 'jhi-doctor-part-payment-detail',
  templateUrl: './doctor-part-payment-detail.component.html'
})
export class DoctorPartPaymentDetailComponent implements OnInit {
  doctorPartPayment: IDoctorPartPayment;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ doctorPartPayment }) => {
      this.doctorPartPayment = doctorPartPayment;
    });
  }

  previousState() {
    window.history.back();
  }
}
