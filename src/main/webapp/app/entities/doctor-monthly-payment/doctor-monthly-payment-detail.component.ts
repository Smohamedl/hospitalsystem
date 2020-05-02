import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';

@Component({
  selector: 'jhi-doctor-monthly-payment-detail',
  templateUrl: './doctor-monthly-payment-detail.component.html'
})
export class DoctorMonthlyPaymentDetailComponent implements OnInit {
  doctorMonthlyPayment: IDoctorMonthlyPayment;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ doctorMonthlyPayment }) => {
      this.doctorMonthlyPayment = doctorMonthlyPayment;
    });
  }

  previousState() {
    window.history.back();
  }
}
