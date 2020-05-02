import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IDoctorPartPayment, DoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';
import { DoctorPartPaymentService } from './doctor-part-payment.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';

@Component({
  selector: 'jhi-doctor-part-payment-update',
  templateUrl: './doctor-part-payment-update.component.html'
})
export class DoctorPartPaymentUpdateComponent implements OnInit {
  isSaving: boolean;

  doctors: IDoctor[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    total: [null, [Validators.required]],
    reference: [],
    date: [null, [Validators.required]],
    doctor: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected doctorPartPaymentService: DoctorPartPaymentService,
    protected doctorService: DoctorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ doctorPartPayment }) => {
      this.updateForm(doctorPartPayment);
    });
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(doctorPartPayment: IDoctorPartPayment) {
    this.editForm.patchValue({
      id: doctorPartPayment.id,
      total: doctorPartPayment.total,
      reference: doctorPartPayment.reference,
      date: doctorPartPayment.date,
      doctor: doctorPartPayment.doctor
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const doctorPartPayment = this.createFromForm();
    if (doctorPartPayment.id !== undefined) {
      this.subscribeToSaveResponse(this.doctorPartPaymentService.update(doctorPartPayment));
    } else {
      this.subscribeToSaveResponse(this.doctorPartPaymentService.create(doctorPartPayment));
    }
  }

  private createFromForm(): IDoctorPartPayment {
    return {
      ...new DoctorPartPayment(),
      id: this.editForm.get(['id']).value,
      total: this.editForm.get(['total']).value,
      reference: this.editForm.get(['reference']).value,
      date: this.editForm.get(['date']).value,
      doctor: this.editForm.get(['doctor']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoctorPartPayment>>) {
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
