import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IDoctorMonthlyPayment, DoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';
import { DoctorMonthlyPaymentService } from './doctor-monthly-payment.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';

@Component({
  selector: 'jhi-doctor-monthly-payment-update',
  templateUrl: './doctor-monthly-payment-update.component.html'
})
export class DoctorMonthlyPaymentUpdateComponent implements OnInit {
  isSaving: boolean;

  doctors: IDoctor[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    paid: [null, [Validators.required]],
    date: [null, [Validators.required]],
    reference: [],
    doctor: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected doctorMonthlyPaymentService: DoctorMonthlyPaymentService,
    protected doctorService: DoctorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ doctorMonthlyPayment }) => {
      this.updateForm(doctorMonthlyPayment);
    });
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(doctorMonthlyPayment: IDoctorMonthlyPayment) {
    this.editForm.patchValue({
      id: doctorMonthlyPayment.id,
      paid: doctorMonthlyPayment.paid,
      date: doctorMonthlyPayment.date,
      reference: doctorMonthlyPayment.reference,
      doctor: doctorMonthlyPayment.doctor
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const doctorMonthlyPayment = this.createFromForm();
    if (doctorMonthlyPayment.id !== undefined) {
      this.subscribeToSaveResponse(this.doctorMonthlyPaymentService.update(doctorMonthlyPayment));
    } else {
      this.subscribeToSaveResponse(this.doctorMonthlyPaymentService.create(doctorMonthlyPayment));
    }
  }

  private createFromForm(): IDoctorMonthlyPayment {
    return {
      ...new DoctorMonthlyPayment(),
      id: this.editForm.get(['id']).value,
      paid: this.editForm.get(['paid']).value,
      date: this.editForm.get(['date']).value,
      reference: this.editForm.get(['reference']).value,
      doctor: this.editForm.get(['doctor']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoctorMonthlyPayment>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackDoctorById(index: number, item: IDoctor) {
    return item.id;
  }
}
