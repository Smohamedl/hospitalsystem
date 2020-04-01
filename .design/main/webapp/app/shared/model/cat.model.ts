import { IMedicalService } from 'app/shared/model/medical-service.model';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface ICat {
  id?: number;
  medicalService?: IMedicalService;
  doctor?: IDoctor;
}

export class Cat implements ICat {
  constructor(
    public id?: number,
    public medicalService?: IMedicalService,
    public doctor?: IDoctor
  ) {}
}
