import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IHoraire_garde, Horaire_garde } from 'app/shared/model/horaire-garde.model';
import { Horaire_gardeService } from './horaire-garde.service';

@Component({
  selector: 'jhi-horaire-garde-update',
  templateUrl: './horaire-garde-update.component.html'
})
export class Horaire_gardeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    start: [],
    end: [],
    name: []
  });

  constructor(protected horaire_gardeService: Horaire_gardeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ horaire_garde }) => {
      this.updateForm(horaire_garde);
    });
  }

  updateForm(horaire_garde: IHoraire_garde) {
    this.editForm.patchValue({
      id: horaire_garde.id,
      start: horaire_garde.start,
      end: horaire_garde.end,
      name: horaire_garde.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const horaire_garde = this.createFromForm();
    if (horaire_garde.id !== undefined) {
      this.subscribeToSaveResponse(this.horaire_gardeService.update(horaire_garde));
    } else {
      this.subscribeToSaveResponse(this.horaire_gardeService.create(horaire_garde));
    }
  }

  private createFromForm(): IHoraire_garde {
    return {
      ...new Horaire_garde(),
      id: this.editForm.get(['id']).value,
      start: this.editForm.get(['start']).value,
      end: this.editForm.get(['end']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoraire_garde>>) {
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
