import { Moment } from 'moment';
import { IPatient } from 'app/shared/model/patient.model';
import { IMedicalService } from 'app/shared/model/medical-service.model';

export interface IHospitalization {
  id?: number;
  date?: Moment;
  description?: string;
  patient?: IPatient;
  medicalService?: IMedicalService;
}

export class Hospitalization implements IHospitalization {
  constructor(
    public id?: number,
    public date?: Moment,
    public description?: string,
    public patient?: IPatient,
    public medicalService?: IMedicalService
  ) {}
}
