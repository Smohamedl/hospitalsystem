import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IProvidedr, Providedr } from 'app/shared/model/provider.model';
import { ProviderService } from './provider.service';

@Component({
  selector: 'jhi-providedr-update',
  templateUrl: './provider-update.component.html'
})
export class ProviderUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    tel: [],
    adress: []
  });

  constructor(protected providedrService: ProviderService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ providedr }) => {
      this.updateForm(providedr);
    });
  }

  updateForm(providedr: IProvidedr) {
    this.editForm.patchValue({
      id: providedr.id,
      name: providedr.name,
      tel: providedr.tel,
      adress: providedr.adress
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const providedr = this.createFromForm();
    if (providedr.id !== undefined) {
      this.subscribeToSaveResponse(this.providedrService.update(providedr));
    } else {
      this.subscribeToSaveResponse(this.providedrService.create(providedr));
    }
  }

  private createFromForm(): IProvidedr {
    return {
      ...new Providedr(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      tel: this.editForm.get(['tel']).value,
      adress: this.editForm.get(['adress']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvidedr>>) {
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
