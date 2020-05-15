import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IQuantityPrice, QuantityPrice } from 'app/shared/model/quantity-price.model';
import { QuantityPriceService } from './quantity-price.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { IOrder } from 'app/shared/model/order.model';
import { OrderService } from 'app/entities/order/order.service';

@Component({
  selector: 'jhi-quantity-price-update',
  templateUrl: './quantity-price-update.component.html'
})
export class QuantityPriceUpdateComponent implements OnInit {
  isSaving: boolean;

  products: IProduct[];

  orders: IOrder[];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required, Validators.min(0)]],
    price: [null, [Validators.required, Validators.min(0)]],
    product: [null, Validators.required],
    order: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected quantityPriceService: QuantityPriceService,
    protected productService: ProductService,
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ quantityPrice }) => {
      this.updateForm(quantityPrice);
    });
    this.productService
      .query()
      .subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.orderService
      .query()
      .subscribe((res: HttpResponse<IOrder[]>) => (this.orders = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(quantityPrice: IQuantityPrice) {
    this.editForm.patchValue({
      id: quantityPrice.id,
      quantity: quantityPrice.quantity,
      price: quantityPrice.price,
      product: quantityPrice.product,
      order: quantityPrice.order
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const quantityPrice = this.createFromForm();
    if (quantityPrice.id !== undefined) {
      this.subscribeToSaveResponse(this.quantityPriceService.update(quantityPrice));
    } else {
      this.subscribeToSaveResponse(this.quantityPriceService.create(quantityPrice));
    }
  }

  private createFromForm(): IQuantityPrice {
    return {
      ...new QuantityPrice(),
      id: this.editForm.get(['id']).value,
      quantity: this.editForm.get(['quantity']).value,
      price: this.editForm.get(['price']).value,
      product: this.editForm.get(['product']).value,
      order: this.editForm.get(['order']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuantityPrice>>) {
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

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }

  trackOrderById(index: number, item: IOrder) {
    return item.id;
  }
}
