import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { IGuard, Guard } from 'app/shared/model/guard.model';
import { GuardService } from './guard.service';

@Component({
  selector: 'jhi-guard-update',
  templateUrl: './guard-update.component.html'
})
export class GuardUpdateComponent implements OnInit {
  isSaving: boolean;
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    pay: [null, [Validators.required]],
    date: [null, [Validators.required]]
  });

  constructor(protected guardService: GuardService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ guard }) => {
      this.updateForm(guard);
    });
  }

  updateForm(guard: IGuard) {
    this.editForm.patchValue({
      id: guard.id,
      pay: guard.pay,
      date: guard.date
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
      pay: this.editForm.get(['pay']).value,
      date: this.editForm.get(['date']).value
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
}
