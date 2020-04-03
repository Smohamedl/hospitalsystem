import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ISocialOrganizationRegimen, SocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';
import { SocialOrganizationRegimenService } from './social-organization-regimen.service';
import { ISocialOrganization } from 'app/shared/model/social-organization.model';
import { SocialOrganizationService } from 'app/entities/social-organization/social-organization.service';

@Component({
  selector: 'jhi-social-organization-regimen-update',
  templateUrl: './social-organization-regimen-update.component.html'
})
export class SocialOrganizationRegimenUpdateComponent implements OnInit {
  isSaving: boolean;

  socialorganizations: ISocialOrganization[];

  editForm = this.fb.group({
    id: [],
    percentage: [null, [Validators.required, Validators.min(1), Validators.max(100)]],
    socialOrganization: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected socialOrganizationRegimenService: SocialOrganizationRegimenService,
    protected socialOrganizationService: SocialOrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ socialOrganizationRegimen }) => {
      this.updateForm(socialOrganizationRegimen);
    });
    this.socialOrganizationService
      .query()
      .subscribe(
        (res: HttpResponse<ISocialOrganization[]>) => (this.socialorganizations = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(socialOrganizationRegimen: ISocialOrganizationRegimen) {
    this.editForm.patchValue({
      id: socialOrganizationRegimen.id,
      percentage: socialOrganizationRegimen.percentage,
      socialOrganization: socialOrganizationRegimen.socialOrganization
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const socialOrganizationRegimen = this.createFromForm();
    if (socialOrganizationRegimen.id !== undefined) {
      this.subscribeToSaveResponse(this.socialOrganizationRegimenService.update(socialOrganizationRegimen));
    } else {
      this.subscribeToSaveResponse(this.socialOrganizationRegimenService.create(socialOrganizationRegimen));
    }
  }

  private createFromForm(): ISocialOrganizationRegimen {
    return {
      ...new SocialOrganizationRegimen(),
      id: this.editForm.get(['id']).value,
      percentage: this.editForm.get(['percentage']).value,
      socialOrganization: this.editForm.get(['socialOrganization']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISocialOrganizationRegimen>>) {
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

  trackSocialOrganizationByName(index: number, item: ISocialOrganization) {
    return item.name;
  }
}
