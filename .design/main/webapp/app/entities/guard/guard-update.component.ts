import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IGuard, Guard } from 'app/shared/model/guard.model';
import { GuardService } from './guard.service';
import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';
import { GuardScheduleService } from 'app/entities/guard-schedule/guard-schedule.service';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';

@Component({
  selector: 'jhi-guard-update',
  templateUrl: './guard-update.component.html'
})
export class GuardUpdateComponent implements OnInit {
  isSaving: boolean;

  guardschedules: IGuardSchedule[];

  medicalservices: IMedicalService[];

  doctors: IDoctor[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    guardSchedule: [null, Validators.required],
    doctorMedicalService: [null, Validators.required],
    doctor: [null, Validators.required]
  });

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,

    protected jhiAlertService: JhiAlertService,
    protected guardService: GuardService,
    protected guardScheduleService: GuardScheduleService,
    protected medicalServiceService: MedicalServiceService,
    protected doctorService: DoctorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ guard }) => {
      this.updateForm(guard);
    });
    this.guardScheduleService
      .query()
      .subscribe(
        (res: HttpResponse<IGuardSchedule[]>) => (this.guardschedules = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  onChangeService() {
    this.doctorService
      .findByService(this.editForm.get(['doctorMedicalService']).value.name)
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(guard: IGuard) {
    this.editForm.patchValue({
      id: guard.id,
      date: guard.date,
      guardSchedule: guard.guardSchedule,
      doctorMedicalService: guard.doctorMedicalService,
      doctor: guard.doctor
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const guard = this.createFromForm();
    if (guard.id !== undefined) {
      this.subscribeToSaveResponse(this.guardService.update(guard));
    } else {
      this.subscribeToSaveResponse(this.guardService.create(guard));
    }
  }

  private createFromForm(): IGuard {
    return {
      ...new Guard(),
      id: this.editForm.get(['id']).value,
      date: this.editForm.get(['date']).value,
      guardSchedule: this.editForm.get(['guardSchedule']).value,
      doctorMedicalService: this.editForm.get(['doctorMedicalService']).value,
      doctor: this.editForm.get(['doctor']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuard>>) {
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

  trackGuardScheduleById(index: number, item: IGuardSchedule) {
    return item.id;
  }

  trackMedicalServiceById(index: number, item: IMedicalService) {
    return item.id;
  }

  trackDoctorById(index: number, item: IDoctor) {
    return item.id;
  }
}
