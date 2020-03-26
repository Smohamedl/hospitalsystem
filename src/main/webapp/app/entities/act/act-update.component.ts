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
import { IActype } from 'app/shared/model/actype.model';
import { ActypeService } from 'app/entities/actype/actype.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';
import * as fileSaver from 'file-saver';

@Component({
  selector: 'jhi-act-update',
  templateUrl: './act-update.component.html'
})
export class ActUpdateComponent implements OnInit {
  isSaving: boolean;

  medicalservices: IMedicalService[];

  actypes: IActype[];

  doctors: IDoctor[];

  patients: IPatient[];

  patientStr : string[];

  selectedPatientName: string;

  editForm = this.fb.group({
    id: [],
    patientName: [null, [Validators.required]],
    medicalService: [null, Validators.required],
    actype: [null, Validators.required],
    doctor: [null, Validators.required],
    patient: [null]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected actService: ActService,
    protected medicalServiceService: MedicalServiceService,
    protected actypeService: ActypeService,
    protected doctorService: DoctorService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
	  this.patientStr = [];
  }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ act }) => {
      this.updateForm(act);
    });
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.actypeService
      .query()
      .subscribe((res: HttpResponse<IActype[]>) => (this.actypes = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.patientService
      .query()
      .subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(act: IAct) {
    this.editForm.patchValue({
      id: act.id,
      patientName: act.patientName,
      medicalService: act.medicalService,
      actype: act.actype,
      doctor: act.doctor,
      patient: act.patient
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const act = this.createFromForm();
    if (act.id !== undefined) {
      this.actService.update(act).subscribe(response => {
        const filename = "recu.pdf";
 
        this.saveFile(response.body, filename);

		this.previousState();
      });
    } else {
      this.actService.create(act).subscribe(response => {
        const filename = "recu.pdf";
 
        this.saveFile(response.body, filename);

		this.previousState();
      });
    }
  }

  saveFile(data: any, filename?: string) {
    const blob = new Blob([data], {type: 'application/pdf; charset=utf-8'});
    fileSaver.saveAs(blob, filename);
  }

  onChangeService(){
	 this.actypeService
      .findByService(this.editForm.get(['medicalService']).value.name)
      .subscribe((res: HttpResponse<IActype[]>) => (this.actypes = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    
	 this.doctorService
     .findByService(this.editForm.get(['medicalService']).value.name)
     .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    
  }

  onSelectPatient(pname: string){
	this.selectedPatientName = pname;
	this.editForm.value.patientName = pname;
  }

  getPatients(event){
	this.patientService
      .query()
      .subscribe((res: HttpResponse<IPatient[]>) => {
						this.patients = res.body;
					 	this.patients = this.patients.filter(e => e.firstname.startsWith(event.query));
													}
						, (res: HttpErrorResponse) => this.onError(res.message));
  }

  private createFromForm(): IAct {
    return {
      ...new Act(),
      id: this.editForm.get(['id']).value,
      patientName: this.editForm.get(['patientName']).value,
      medicalService: this.editForm.get(['medicalService']).value,
      actype: this.editForm.get(['actype']).value,
      doctor: this.editForm.get(['doctor']).value,
      patient: this.editForm.get(['patient']).value
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

  trackActypeById(index: number, item: IActype) {
    return item.id;
  }

  trackDoctorById(index: number, item: IDoctor) {
    return item.id;
  }

  trackPatientById(index: number, item: IPatient) {
    return item.id;
  }
}
