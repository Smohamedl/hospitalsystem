import { IProduct } from 'app/shared/model/product.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IQuantityPrice {
  id?: number;
  quantity?: number;
  price?: number;
  product?: IProduct;
  order?: IOrder;
}

export class QuantityPrice implements IQuantityPrice {
  constructor(public id?: number, public quantity?: number, public price?: number, public product?: IProduct, public order?: IOrder) {}
}
