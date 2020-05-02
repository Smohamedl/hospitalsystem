import { IAct } from 'app/shared/model/act.model';

export interface IPaymentMethod {
  id?: number;
  name?: string;
  acts?: IAct[];
}

export class PaymentMethod implements IPaymentMethod {
  constructor(public id?: number, public name?: string, public acts?: IAct[]) {}
}
