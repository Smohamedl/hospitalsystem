import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IFixedDoctorPayment, FixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';
import { FixedDoctorPaymentService } from './fixed-doctor-payment.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';

@Component({
  selector: 'jhi-fixed-doctor-payment-update',
  templateUrl: './fixed-doctor-payment-update.component.html'
})
export class FixedDoctorPaymentUpdateComponent implements OnInit {
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
    protected fixedDoctorPaymentService: FixedDoctorPaymentService,
    protected doctorService: DoctorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ fixedDoctorPayment }) => {
      this.updateForm(fixedDoctorPayment);
    });
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(fixedDoctorPayment: IFixedDoctorPayment) {
    this.editForm.patchValue({
      id: fixedDoctorPayment.id,
      paid: fixedDoctorPayment.paid,
      date: fixedDoctorPayment.date,
      reference: fixedDoctorPayment.reference,
      doctor: fixedDoctorPayment.doctor
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const fixedDoctorPayment = this.createFromForm();
    if (fixedDoctorPayment.id !== undefined) {
      this.subscribeToSaveResponse(this.fixedDoctorPaymentService.update(fixedDoctorPayment));
    } else {
      this.subscribeToSaveResponse(this.fixedDoctorPaymentService.create(fixedDoctorPayment));
    }
  }

  private createFromForm(): IFixedDoctorPayment {
    return {
      ...new FixedDoctorPayment(),
      id: this.editForm.get(['id']).value,
      paid: this.editForm.get(['paid']).value,
      date: this.editForm.get(['date']).value,
      reference: this.editForm.get(['reference']).value,
      doctor: this.editForm.get(['doctor']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedDoctorPayment>>) {
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
