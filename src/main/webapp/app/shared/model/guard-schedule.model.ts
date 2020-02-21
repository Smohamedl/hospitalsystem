export interface IGuardSchedule {
  id?: number;
  start?: number;
  end?: number;
  name?: string;
}

export class GuardSchedule implements IGuardSchedule {
  constructor(public id?: number, public start?: number, public end?: number, public name?: string) {}
}
