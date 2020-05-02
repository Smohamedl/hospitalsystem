import { Moment } from 'moment';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface IDoctorMonthlyPayment {
  id?: number;
  paid?: boolean;
  date?: Moment;
  reference?: string;
  doctor?: IDoctor;
}

export class DoctorMonthlyPayment implements IDoctorMonthlyPayment {
  constructor(public id?: number, public paid?: boolean, public date?: Moment, public reference?: string, public doctor?: IDoctor) {
    this.paid = this.paid || false;
  }
}
