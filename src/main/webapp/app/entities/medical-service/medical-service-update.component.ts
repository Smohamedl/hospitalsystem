import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IMedicalService, MedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from './medical-service.service';

@Component({
  selector: 'jhi-medical-service-update',
  templateUrl: './medical-service-update.component.html'
})
export class MedicalServiceUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected medicalServiceService: MedicalServiceService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ medicalService }) => {
      this.updateForm(medicalService);
    });
  }

  updateForm(medicalService: IMedicalService) {
    this.editForm.patchValue({
      id: medicalService.id,
      name: medicalService.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const medicalService = this.createFromForm();
    if (medicalService.id !== undefined) {
      this.subscribeToSaveResponse(this.medicalServiceService.update(medicalService));
    } else {
      this.subscribeToSaveResponse(this.medicalServiceService.create(medicalService));
    }
  }

  private createFromForm(): IMedicalService {
    return {
      ...new MedicalService(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalService>>) {
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
