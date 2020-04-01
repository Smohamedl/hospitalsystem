import { Moment } from 'moment';
import { IAct } from 'app/shared/model/act.model';

export interface IReceiptAct {
  id?: number;
  total?: number;
  paid?: boolean;
  paidDoctor?: boolean;
  date?: Moment;
  act?: IAct;
}

export class ReceiptAct implements IReceiptAct {
  constructor(
    public id?: number,
    public total?: number,
    public paid?: boolean,
    public paidDoctor?: boolean,
    public date?: Moment,
    public act?: IAct
  ) {
    this.paid = this.paid || false;
    this.paidDoctor = this.paidDoctor || false;
  }
}
