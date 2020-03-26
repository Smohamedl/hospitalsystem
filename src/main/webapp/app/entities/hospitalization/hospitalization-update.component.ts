import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { IHospitalization, Hospitalization } from 'app/shared/model/hospitalization.model';
import { HospitalizationService } from './hospitalization.service';

@Component({
  selector: 'jhi-hospitalization-update',
  templateUrl: './hospitalization-update.component.html'
})
export class HospitalizationUpdateComponent implements OnInit {
  isSaving: boolean;
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]]
  });

  constructor(
    protected hospitalizationService: HospitalizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ hospitalization }) => {
      this.updateForm(hospitalization);
    });
  }

  updateForm(hospitalization: IHospitalization) {
    this.editForm.patchValue({
      id: hospitalization.id,
      date: hospitalization.date
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
      date: this.editForm.get(['date']).value
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
}
