import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IStock, Stock } from 'app/shared/model/stock.model';
import { StockService } from './stock.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

@Component({
  selector: 'jhi-stock-update',
  templateUrl: './stock-update.component.html'
})
export class StockUpdateComponent implements OnInit {
  isSaving: boolean;

  products: IProduct[];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required]],
    product: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected stockService: StockService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ stock }) => {
      this.updateForm(stock);
    });
    this.productService.query({ filter: 'stock-is-null' }).subscribe(
      (res: HttpResponse<IProduct[]>) => {
        if (!this.editForm.get('product').value || !this.editForm.get('product').value.id) {
          this.products = res.body;
        } else {
          this.productService
            .find(this.editForm.get('product').value.id)
            .subscribe(
              (subRes: HttpResponse<IProduct>) => (this.products = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(stock: IStock) {
    this.editForm.patchValue({
      id: stock.id,
      quantity: stock.quantity,
      product: stock.product
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const stock = this.createFromForm();
    if (stock.id !== undefined) {
      this.subscribeToSaveResponse(this.stockService.update(stock));
    } else {
      this.subscribeToSaveResponse(this.stockService.create(stock));
    }
  }

  private createFromForm(): IStock {
    return {
      ...new Stock(),
      id: this.editForm.get(['id']).value,
      quantity: this.editForm.get(['quantity']).value,
      product: this.editForm.get(['product']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>) {
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
}
