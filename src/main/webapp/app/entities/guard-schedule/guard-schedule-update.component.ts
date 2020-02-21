import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IGuardSchedule, GuardSchedule } from 'app/shared/model/guard-schedule.model';
import { GuardScheduleService } from './guard-schedule.service';

@Component({
  selector: 'jhi-guard-schedule-update',
  templateUrl: './guard-schedule-update.component.html'
})
export class GuardScheduleUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    start: [],
    end: [],
    name: []
  });

  constructor(protected guardScheduleService: GuardScheduleService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ guardSchedule }) => {
      this.updateForm(guardSchedule);
    });
  }

  updateForm(guardSchedule: IGuardSchedule) {
    this.editForm.patchValue({
      id: guardSchedule.id,
      start: guardSchedule.start,
      end: guardSchedule.end,
      name: guardSchedule.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const guardSchedule = this.createFromForm();
    if (guardSchedule.id !== undefined) {
      this.subscribeToSaveResponse(this.guardScheduleService.update(guardSchedule));
    } else {
      this.subscribeToSaveResponse(this.guardScheduleService.create(guardSchedule));
    }
  }

  private createFromForm(): IGuardSchedule {
    return {
      ...new GuardSchedule(),
      id: this.editForm.get(['id']).value,
      start: this.editForm.get(['start']).value,
      end: this.editForm.get(['end']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuardSchedule>>) {
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
