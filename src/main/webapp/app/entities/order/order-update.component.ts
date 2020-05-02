import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';
import { IProvider } from 'app/shared/model/provider.model';
import { ProviderService } from 'app/entities/provider/provider.service';
import { ProductService } from 'app/entities/product/product.service';
import { IProduct } from 'app/shared/model/product.model';
import { IQuantityPrice } from 'app/shared/model/quantity-price.model';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
  isSaving: boolean;

  providers: IProvider[];
  quantityPrice: IQuantityPrice[];
  macquantityPrice: IQuantityPrice[][];
  productsList: IProduct[];

  editForm = this.fb.group({
    id: [],
    provider: [null, Validators.required],
    formQuantityPrice: this.fb.array([
      this.fb.group({
        id: [],
        quantity: [null, [Validators.required, Validators.min(0)]],
        price: [null, [Validators.required, Validators.min(0)]],
        product: [null, Validators.required],
        order: [null, Validators.required]
      })
    ])
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected orderService: OrderService,
    protected providerService: ProviderService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.macquantityPrice = [];
    this.activatedRoute.data.subscribe(({ order }) => {
      this.updateForm(order);
    });

    this.providerService
      .query()
      .subscribe((res: HttpResponse<IProvider[]>) => (this.providers = res.body), (res: HttpErrorResponse) => this.onError(res.message));

    this.productService
      .query()
      .subscribe((res: HttpResponse<IProduct[]>) => (this.productsList = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(order: IOrder) {
    this.editForm.patchValue({
      id: order.id,
      provider: order.provider,
      formQuantityPrice: new FormArray([])
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  private createFromForm(): { provider: any; id: any; quantityPrices?: IQuantityPrice[] } {
    return {
      ...new Order(),
      id: this.editForm.get(['id']).value,
      provider: this.editForm.get(['provider']).value,
      quantityPrices: this.editForm.get(['formQuantityPrice']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>) {
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

  get formQuantityPrice(): FormArray {
    return this.editForm.get('formQuantityPrice') as FormArray;
  }

  trackProviderById(index: number, item: IProvider) {
    return item.id;
  }

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }

  addProductQuantity() {
    this.formQuantityPrice.push(new FormControl());
  }
}
