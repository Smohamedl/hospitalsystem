export interface IPatient {
  id?: number;
  name?: string;
  firstname?: string;
  tel?: string;
  address?: string;
}

export class Patient implements IPatient {
  constructor(public id?: number, public name?: string, public firstname?: string, public tel?: string, public address?: string) {}
}
