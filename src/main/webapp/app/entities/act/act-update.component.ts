import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IAct, Act } from 'app/shared/model/act.model';
import { ActService } from './act.service';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';
import { IReceiptAct } from 'app/shared/model/receipt-act.model';
import { ReceiptActService } from 'app/entities/receipt-act/receipt-act.service';
import { IActype } from 'app/shared/model/actype.model';
import { ActypeService } from 'app/entities/actype/actype.service';
import { FormArray, FormControl } from '@angular/forms';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';

@Component({
  selector: 'jhi-act-update',
  templateUrl: './act-update.component.html'
})
export class ActUpdateComponent implements OnInit {
  isSaving: boolean;

  medicalservices: IMedicalService[];

  doctors: IDoctor[];

  patients: IPatient[];

  receiptacts: IReceiptAct[];

  actypes: IActype[];

  mactypes: IActype[][];

  paymentmethods: IPaymentMethod[];

  declarationNumber: String;

  editForm = this.fb.group({
    id: [],
    patientName: [null, [Validators.required]],
    medicalService: [null, Validators.required],
    doctor: [null, Validators.required],
    patient: [],
    receiptAct: [],
    formActypes: new FormArray([]),
    formMedicalServices: new FormArray([]),
    paymentMethod: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected actService: ActService,
    protected medicalServiceService: MedicalServiceService,
    protected doctorService: DoctorService,
    protected patientService: PatientService,
    protected receiptActService: ReceiptActService,
    protected actypeService: ActypeService,
    protected paymentMethodService: PaymentMethodService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.declarationNumber = 'Non declaré';
  }

  ngOnInit() {
    this.isSaving = false;
    this.mactypes = [];
    this.activatedRoute.data.subscribe(({ act }) => {
      this.updateForm(act);
    });
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.patientService
      .query()
      .subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.receiptActService.query({ filter: 'act-is-null' }).subscribe(
      (res: HttpResponse<IReceiptAct[]>) => {
        if (!this.editForm.get('receiptAct').value || !this.editForm.get('receiptAct').value.id) {
          this.receiptacts = res.body;
        } else {
          this.receiptActService
            .find(this.editForm.get('receiptAct').value.id)
            .subscribe(
              (subRes: HttpResponse<IReceiptAct>) => (this.receiptacts = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.actypeService
      .query()
      .subscribe((res: HttpResponse<IActype[]>) => (this.actypes = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.paymentMethodService
      .query()
      .subscribe(
        (res: HttpResponse<IPaymentMethod[]>) => (this.paymentmethods = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(act: IAct) {
    this.editForm.patchValue({
      id: act.id,
      patientName: act.patientName,
      medicalService: act.medicalService,
      doctor: act.doctor,
      patient: act.patient,
      receiptAct: act.receiptAct,
      formActypes: new FormArray([]),
      formMedicalServices: new FormArray([]),
      paymentMethod: act.paymentMethod
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const act = this.createFromForm();
    console.log('############ ' + act);
    if (act.id !== undefined) {
      this.subscribeToSaveResponse(this.actService.update(act));
    } else {
      this.subscribeToSaveResponse(this.actService.create(act));
    }
  }

  private createFromForm(): IAct {
    return {
      ...new Act(),
      id: this.editForm.get(['id']).value,
      patientName: this.editForm.get(['patientName']).value,
      medicalService: this.editForm.get(['medicalService']).value,
      doctor: this.editForm.get(['doctor']).value,
      patient: this.editForm.get(['patient']).value,
      receiptAct: this.editForm.get(['receiptAct']).value,
      actypes: this.editForm.get(['formActypes']).value,
      paymentMethod: this.editForm.get(['paymentMethod']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAct>>) {
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

  trackMedicalServiceById(index: number, item: IMedicalService) {
    return item.id;
  }

  trackDoctorById(index: number, item: IDoctor) {
    return item.id;
  }

  trackPatientById(index: number, item: IPatient) {
    return item.id;
  }

  trackReceiptActById(index: number, item: IReceiptAct) {
    return item.id;
  }
  somethingChanged() {
    if (this.editForm.get('patient').value.socialOrganizationDetails != null) {
      this.declarationNumber = this.editForm.get('patient').value.socialOrganizationDetails.registrationNumber;
    } else {
      this.declarationNumber = 'Non declaré';
    }
  }
  trackPaymentMethodById(index: number, item: IPaymentMethod) {
    return item.id;
  }

  trackActypeById(index: number, item: IActype) {
    return item.id;
  }

  get formActypes(): FormArray {
    return this.editForm.get('formActypes') as FormArray;
  }

  get formMedicalServices(): FormArray {
    return this.editForm.get('formMedicalServices') as FormArray;
  }

  addCity() {
    // get actypes
    this.actypeService
      .query()
      .subscribe((res: HttpResponse<IActype[]>) => this.mactypes.push(res.body), (res: HttpErrorResponse) => this.onError(res.message));

    this.formMedicalServices.push(new FormControl());
    this.formActypes.push(new FormControl());
  }

  onChangeService() {
    this.doctorService
      .findByService(this.editForm.get(['medicalService']).value.name)
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  onChangeAddedService(index: number) {
    console.log('################## index ' + index);

    this.actypeService
      .findByService(this.editForm.get(['formMedicalServices']).at(index).value.name)
      .subscribe(
        (res: HttpResponse<IActype[]>) => (this.mactypes[index] = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }
}
