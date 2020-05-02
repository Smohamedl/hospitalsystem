import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IProvider, Provider } from 'app/shared/model/provider.model';
import { ProviderService } from './provider.service';

@Component({
  selector: 'jhi-Provider-update',
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

  constructor(protected providerService: ProviderService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ provider }) => {
      this.updateForm(provider);
    });
  }

  updateForm(provider: IProvider) {
    this.editForm.patchValue({
      id: provider.id,
      name: provider.name,
      tel: provider.tel,
      adress: provider.adress
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const provider = this.createFromForm();
    if (provider.id !== undefined) {
      this.subscribeToSaveResponse(this.providerService.update(provider));
    } else {
      this.subscribeToSaveResponse(this.providerService.create(provider));
    }
  }

  private createFromForm(): IProvider {
    return {
      ...new Provider(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      tel: this.editForm.get(['tel']).value,
      adress: this.editForm.get(['adress']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvider>>) {
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
