import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ISocialOrganizationDetails, SocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';
import { SocialOrganizationDetailsService } from './social-organization-details.service';
import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';
import { SocialOrganizationRegimenService } from 'app/entities/social-organization-regimen/social-organization-regimen.service';

@Component({
  selector: 'jhi-social-organization-details-update',
  templateUrl: './social-organization-details-update.component.html'
})
export class SocialOrganizationDetailsUpdateComponent implements OnInit {
  isSaving: boolean;

  socialorganizationregimen: ISocialOrganizationRegimen[];

  editForm = this.fb.group({
    id: [],
    registrationNumber: [null, [Validators.required, Validators.min(1)]],
    matriculeNumber: [null, [Validators.required, Validators.minLength(1)]],
    socialOrganizationRegimen: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected socialOrganizationDetailsService: SocialOrganizationDetailsService,
    protected socialOrganizationRegimenService: SocialOrganizationRegimenService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ socialOrganizationDetails }) => {
      this.updateForm(socialOrganizationDetails);
    });
    this.socialOrganizationRegimenService
      .query()
      .subscribe(
        (res: HttpResponse<ISocialOrganizationRegimen[]>) => (this.socialorganizationregimen = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(socialOrganizationDetails: ISocialOrganizationDetails) {
    this.editForm.patchValue({
      id: socialOrganizationDetails.id,
      registrationNumber: socialOrganizationDetails.registrationNumber,
      matriculeNumber: socialOrganizationDetails.matriculeNumber,
      socialOrganizationRegimen: socialOrganizationDetails.socialOrganizationRegimen
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const socialOrganizationDetails = this.createFromForm();
    if (socialOrganizationDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.socialOrganizationDetailsService.update(socialOrganizationDetails));
    } else {
      this.subscribeToSaveResponse(this.socialOrganizationDetailsService.create(socialOrganizationDetails));
    }
  }

  private createFromForm(): ISocialOrganizationDetails {
    return {
      ...new SocialOrganizationDetails(),
      id: this.editForm.get(['id']).value,
      registrationNumber: this.editForm.get(['registrationNumber']).value,
      matriculeNumber: this.editForm.get(['matriculeNumber']).value,
      socialOrganizationRegimen: this.editForm.get(['socialOrganizationRegimen']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISocialOrganizationDetails>>) {
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

  trackSocialOrganizationRegimenById(index: number, item: ISocialOrganizationRegimen) {
    return item.id;
  }
}
