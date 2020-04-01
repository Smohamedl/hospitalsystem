import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IHospitalization, Hospitalization } from 'app/shared/model/hospitalization.model';
import { HospitalizationService } from './hospitalization.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';

@Component({
  selector: 'jhi-hospitalization-update',
  templateUrl: './hospitalization-update.component.html'
})
export class HospitalizationUpdateComponent implements OnInit {
  isSaving: boolean;

  patients: IPatient[];

  medicalservices: IMedicalService[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    description: [null, [Validators.required]],
    patient: [null, Validators.required],
    medicalService: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected hospitalizationService: HospitalizationService,
    protected patientService: PatientService,
    protected medicalServiceService: MedicalServiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ hospitalization }) => {
      this.updateForm(hospitalization);
    });
    this.patientService
      .query()
      .subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(hospitalization: IHospitalization) {
    this.editForm.patchValue({
      id: hospitalization.id,
      date: hospitalization.date,
      description: hospitalization.description,
      patient: hospitalization.patient,
      medicalService: hospitalization.medicalService
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const hospitalization = this.createFromForm();
    if (hospitalization.id !== undefined) {
      this.subscribeToSaveResponse(this.hospitalizationService.update(hospitalization));
    } else {
      this.subscribeToSaveResponse(this.hospitalizationService.create(hospitalization));
    }
  }

  private createFromForm(): IHospitalization {
    return {
      ...new Hospitalization(),
      id: this.editForm.get(['id']).value,
      date: this.editForm.get(['date']).value,
      description: this.editForm.get(['description']).value,
      patient: this.editForm.get(['patient']).value,
      medicalService: this.editForm.get(['medicalService']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHospitalization>>) {
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

  trackPatientById(index: number, item: IPatient) {
    return item.id;
  }

  trackMedicalServiceById(index: number, item: IMedicalService) {
    return item.id;
  }
}
