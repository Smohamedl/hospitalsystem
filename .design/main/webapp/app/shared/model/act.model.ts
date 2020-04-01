import { IMedicalService } from 'app/shared/model/medical-service.model';
import { IActype } from 'app/shared/model/actype.model';
import { IDoctor } from 'app/shared/model/doctor.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IReceiptAct } from 'app/shared/model/receipt-act.model';

export interface IAct {
  id?: number;
  patientName?: string;
  medicalService?: IMedicalService;
  actype?: IActype;
  doctor?: IDoctor;
  patient?: IPatient;
  receiptAct?: IReceiptAct;
}

export class Act implements IAct {
  constructor(
    public id?: number,
    public patientName?: string,
    public medicalService?: IMedicalService,
    public actype?: IActype,
    public doctor?: IDoctor,
    public patient?: IPatient,
    public receiptAct?: IReceiptAct
  ) {}
}
