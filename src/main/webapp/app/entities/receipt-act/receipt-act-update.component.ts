import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IReceiptAct, ReceiptAct } from 'app/shared/model/receipt-act.model';
import { ReceiptActService } from './receipt-act.service';
import { IAct } from 'app/shared/model/act.model';
import { ActService } from 'app/entities/act/act.service';

@Component({
  selector: 'jhi-receipt-act-update',
  templateUrl: './receipt-act-update.component.html'
})
export class ReceiptActUpdateComponent implements OnInit {
  isSaving: boolean;

  acts: IAct[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    total: [],
    paid: [],
    paidDoctor: [],
    date: [],
    act: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected receiptActService: ReceiptActService,
    protected actService: ActService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ receiptAct }) => {
      this.updateForm(receiptAct);
    });
    this.actService.query({ filter: 'receiptact-is-null' }).subscribe(
      (res: HttpResponse<IAct[]>) => {
        if (!this.editForm.get('act').value || !this.editForm.get('act').value.id) {
          this.acts = res.body;
        } else {
          this.actService
            .find(this.editForm.get('act').value.id)
            .subscribe(
              (subRes: HttpResponse<IAct>) => (this.acts = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(receiptAct: IReceiptAct) {
    this.editForm.patchValue({
      id: receiptAct.id,
      total: receiptAct.total,
      paid: receiptAct.paid,
      paidDoctor: receiptAct.paidDoctor,
      date: receiptAct.date,
      act: receiptAct.act
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const receiptAct = this.createFromForm();
    if (receiptAct.id !== undefined) {
      this.subscribeToSaveResponse(this.receiptActService.update(receiptAct));
    } else {
      this.subscribeToSaveResponse(this.receiptActService.create(receiptAct));
    }
  }

  private createFromForm(): IReceiptAct {
    return {
      ...new ReceiptAct(),
      id: this.editForm.get(['id']).value,
      total: this.editForm.get(['total']).value,
      paid: this.editForm.get(['paid']).value,
      paidDoctor: this.editForm.get(['paidDoctor']).value,
      date: this.editForm.get(['date']).value,
      act: this.editForm.get(['act']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceiptAct>>) {
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

  trackActById(index: number, item: IAct) {
    return item.id;
  }
}
