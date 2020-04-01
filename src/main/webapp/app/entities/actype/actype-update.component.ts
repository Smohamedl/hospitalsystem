import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IActype, Actype } from 'app/shared/model/actype.model';
import { ActypeService } from './actype.service';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';

@Component({
  selector: 'jhi-actype-update',
  templateUrl: './actype-update.component.html'
})
export class ActypeUpdateComponent implements OnInit {
  isSaving: boolean;

  medicalservices: IMedicalService[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    price: [null, [Validators.required]],
    medicalService: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected actypeService: ActypeService,
    protected medicalServiceService: MedicalServiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ actype }) => {
      this.updateForm(actype);
    });
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(actype: IActype) {
    this.editForm.patchValue({
      id: actype.id,
      name: actype.name,
      price: actype.price,
      medicalService: actype.medicalService
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const actype = this.createFromForm();
    if (actype.id !== undefined) {
      this.subscribeToSaveResponse(this.actypeService.update(actype));
    } else {
      this.subscribeToSaveResponse(this.actypeService.create(actype));
    }
  }

  private createFromForm(): IActype {
    return {
      ...new Actype(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      price: this.editForm.get(['price']).value,
      medicalService: this.editForm.get(['medicalService']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActype>>) {
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

  trackMedicalServiceById(index: number, item: IMedicalService) {
    return item.id;
  }
}
