import { Moment } from 'moment';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface IFixedDoctorPayment {
  id?: number;
  paid?: boolean;
  date?: Moment;
  reference?: string;
  doctor?: IDoctor;
}

export class FixedDoctorPayment implements IFixedDoctorPayment {
  constructor(public id?: number, public paid?: boolean, public date?: Moment, public reference?: string, public doctor?: IDoctor) {
    this.paid = this.paid || false;
  }
}
