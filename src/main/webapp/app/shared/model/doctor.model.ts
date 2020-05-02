import { IMedicalService } from 'app/shared/model/medical-service.model';

export interface IDoctor {
  id?: number;
  name?: string;
  firstname?: string;
  job?: string;
  specialist?: boolean;
  address?: string;
  tel?: string;
  salary?: number;
  partOfHospitalIncome?: boolean;
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
    public salary?: number,
    public partOfHospitalIncome?: boolean,
    public medicalService?: IMedicalService
  ) {
    this.specialist = this.specialist || false;
    this.partOfHospitalIncome = this.partOfHospitalIncome || false;
  }
}
