import { IMedicalService } from 'app/shared/model/medical-service.model';
import { IDoctor } from 'app/shared/model/doctor.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IReceiptAct } from 'app/shared/model/receipt-act.model';
import { IActype } from 'app/shared/model/actype.model';

export interface IAct {
  id?: number;
  patientName?: string;
  medicalService?: IMedicalService;
  doctor?: IDoctor;
  patient?: IPatient;
  receiptAct?: IReceiptAct;
  actypes?: IActype[];
}

export class Act implements IAct {
  constructor(
    public id?: number,
    public patientName?: string,
    public medicalService?: IMedicalService,
    public doctor?: IDoctor,
    public patient?: IPatient,
    public receiptAct?: IReceiptAct,
    public actypes?: IActype[]
  ) {}
}
