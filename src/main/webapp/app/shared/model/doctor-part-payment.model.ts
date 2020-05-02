import { Moment } from 'moment';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface IDoctorPartPayment {
  id?: number;
  total?: number;
  reference?: string;
  date?: Moment;
  doctor?: IDoctor;
}

export class DoctorPartPayment implements IDoctorPartPayment {
  constructor(public id?: number, public total?: number, public reference?: string, public date?: Moment, public doctor?: IDoctor) {}
}
