import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { ISocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';
import { SocialOrganizationDetailsService } from 'app/entities/social-organization-details/social-organization-details.service';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html'
})
export class PatientUpdateComponent implements OnInit {
  isSaving: boolean;

  socialorganizationdetails: ISocialOrganizationDetails[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    firstname: [null, [Validators.required]],
    tel: [],
    address: [],
    socialOrganizationDetails: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected patientService: PatientService,
    protected socialOrganizationDetailsService: SocialOrganizationDetailsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);
    });
    this.socialOrganizationDetailsService.query({ filter: 'patient-is-null' }).subscribe(
      (res: HttpResponse<ISocialOrganizationDetails[]>) => {
        if (!this.editForm.get('socialOrganizationDetails').value || !this.editForm.get('socialOrganizationDetails').value.id) {
          this.socialorganizationdetails = res.body;
        } else {
          this.socialOrganizationDetailsService
            .find(this.editForm.get('socialOrganizationDetails').value.id)
            .subscribe(
              (subRes: HttpResponse<ISocialOrganizationDetails>) => (this.socialorganizationdetails = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(patient: IPatient) {
    this.editForm.patchValue({
      id: patient.id,
      name: patient.name,
      firstname: patient.firstname,
      tel: patient.tel,
      address: patient.address,
      socialOrganizationDetails: patient.socialOrganizationDetails
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  private createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      firstname: this.editForm.get(['firstname']).value,
      tel: this.editForm.get(['tel']).value,
      address: this.editForm.get(['address']).value,
      socialOrganizationDetails: this.editForm.get(['socialOrganizationDetails']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>) {
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

  trackSocialOrganizationDetailsById(index: number, item: ISocialOrganizationDetails) {
    return item.id;
  }
}
