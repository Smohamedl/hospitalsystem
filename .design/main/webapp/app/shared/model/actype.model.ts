import { IMedicalService } from 'app/shared/model/medical-service.model';

export interface IActype {
  id?: number;
  name?: string;
  price?: number;
  medicalService?: IMedicalService;
}

export class Actype implements IActype {
  constructor(public id?: number, public name?: string, public price?: number, public medicalService?: IMedicalService) {}
}
