import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuantityPrice } from 'app/shared/model/quantity-price.model';

@Component({
  selector: 'jhi-quantity-price-detail',
  templateUrl: './quantity-price-detail.component.html'
})
export class QuantityPriceDetailComponent implements OnInit {
  quantityPrice: IQuantityPrice;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ quantityPrice }) => {
      this.quantityPrice = quantityPrice;
    });
  }

  previousState() {
    window.history.back();
  }
}
