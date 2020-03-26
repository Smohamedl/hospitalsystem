import { Moment } from 'moment';
import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface IGuard {
  id?: number;
  date?: Moment;
  guardSchedule?: IGuardSchedule;
  doctorMedicalService?: IMedicalService;
  doctor?: IDoctor;
}

export class Guard implements IGuard {
  constructor(
    public id?: number,
    public date?: Moment,
    public guardSchedule?: IGuardSchedule,
    public doctorMedicalService?: IMedicalService,
    public doctor?: IDoctor
  ) {}
}
