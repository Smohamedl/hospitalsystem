import { Moment } from 'moment';

export interface IGuard {
  id?: number;
  pay?: number;
  date?: Moment;
}

export class Guard implements IGuard {
  constructor(public id?: number, public pay?: number, public date?: Moment) {}
}
