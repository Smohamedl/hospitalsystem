import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IDoctor, Doctor } from 'app/shared/model/doctor.model';
import { DoctorService } from './doctor.service';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';

@Component({
  selector: 'jhi-doctor-update',
  templateUrl: './doctor-update.component.html'
})
export class DoctorUpdateComponent implements OnInit {
  isSaving: boolean;

  medicalservices: IMedicalService[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    firstname: [null, [Validators.required]],
    job: [null, [Validators.required]],
    specialist: [],
    address: [],
    tel: [],
    salary: [null, [Validators.required, Validators.min(0)]],
    partOfHospitalIncome: [null, [Validators.required]],
    medicalService: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected doctorService: DoctorService,
    protected medicalServiceService: MedicalServiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ doctor }) => {
      this.updateForm(doctor);
    });
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(doctor: IDoctor) {
    this.editForm.patchValue({
      id: doctor.id,
      name: doctor.name,
      firstname: doctor.firstname,
      job: doctor.job,
      specialist: doctor.specialist,
      address: doctor.address,
      tel: doctor.tel,
      salary: doctor.salary,
      partOfHospitalIncome: doctor.partOfHospitalIncome,
      medicalService: doctor.medicalService
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const doctor = this.createFromForm();
    if (doctor.id !== undefined) {
      this.subscribeToSaveResponse(this.doctorService.update(doctor));
    } else {
      this.subscribeToSaveResponse(this.doctorService.create(doctor));
    }
  }

  private createFromForm(): IDoctor {
    return {
      ...new Doctor(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      firstname: this.editForm.get(['firstname']).value,
      job: this.editForm.get(['job']).value,
      specialist: this.editForm.get(['specialist']).value,
      address: this.editForm.get(['address']).value,
      tel: this.editForm.get(['tel']).value,
      salary: this.editForm.get(['salary']).value,
      partOfHospitalIncome: this.editForm.get(['partOfHospitalIncome']).value,
      medicalService: this.editForm.get(['medicalService']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoctor>>) {
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
}
