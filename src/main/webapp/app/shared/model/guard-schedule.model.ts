export interface IGuardSchedule {
  id?: number;
  payement?: number;
  start?: number;
  end?: number;
  name?: string;
}

export class GuardSchedule implements IGuardSchedule {
  constructor(public id?: number, public payement?: number, public start?: number, public end?: number, public name?: string) {}
}
