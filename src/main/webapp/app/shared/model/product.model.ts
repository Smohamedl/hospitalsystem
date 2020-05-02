import { IStock } from 'app/shared/model/stock.model';
import { ICategory } from 'app/shared/model/category.model';
import { IQuantityPrice } from 'app/shared/model/quantity-price.model';

export interface IProduct {
  id?: number;
  name?: string;
  stock?: IStock;
  category?: ICategory;
  quantityPrices?: IQuantityPrice[];
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public stock?: IStock,
    public category?: ICategory,
    public quantityPrices?: IQuantityPrice[]
  ) {}
}
