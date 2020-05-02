import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ISocialOrganization, SocialOrganization } from 'app/shared/model/social-organization.model';
import { SocialOrganizationService } from './social-organization.service';

@Component({
  selector: 'jhi-social-organization-update',
  templateUrl: './social-organization-update.component.html'
})
export class SocialOrganizationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(
    protected socialOrganizationService: SocialOrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ socialOrganization }) => {
      this.updateForm(socialOrganization);
    });
  }

  updateForm(socialOrganization: ISocialOrganization) {
    this.editForm.patchValue({
      id: socialOrganization.id,
      name: socialOrganization.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const socialOrganization = this.createFromForm();
    if (socialOrganization.id !== undefined) {
      this.subscribeToSaveResponse(this.socialOrganizationService.update(socialOrganization));
    } else {
      this.subscribeToSaveResponse(this.socialOrganizationService.create(socialOrganization));
    }
  }

  private createFromForm(): ISocialOrganization {
    return {
      ...new SocialOrganization(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISocialOrganization>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
