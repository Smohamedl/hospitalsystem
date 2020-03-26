import { Moment } from 'moment';

export interface IHospitalization {
  id?: number;
  date?: Moment;
}

export class Hospitalization implements IHospitalization {
  constructor(public id?: number, public date?: Moment) {}
}
