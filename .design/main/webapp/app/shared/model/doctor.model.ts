import { IMedicalService } from 'app/shared/model/medical-service.model';

export interface IDoctor {
  id?: number;
  name?: string;
  firstname?: string;
  job?: string;
  specialist?: boolean;
  address?: string;
  tel?: string;
  medicalService?: IMedicalService;
}

export class Doctor implements IDoctor {
  constructor(
    public id?: number,
    public name?: string,
    public firstname?: string,
    public job?: string,
    public specialist?: boolean,
    public address?: string,
    public tel?: string,
    public medicalService?: IMedicalService
  ) {
    this.specialist = this.specialist || false;
  }
}
