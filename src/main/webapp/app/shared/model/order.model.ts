import { IProvider } from 'app/shared/model/provider.model';
import { IQuantityPrice } from 'app/shared/model/quantity-price.model';

export interface IOrder {
  id?: number;
  provider?: IProvider;
  quantityPrices?: IQuantityPrice[];
}

export class Order implements IOrder {
  constructor(public id?: number, public provider?: IProvider, public quantityPrices?: IQuantityPrice[]) {}
}
