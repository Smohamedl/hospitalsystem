export interface IMedicalService {
  id?: number;
  name?: string;
}

export class MedicalService implements IMedicalService {
  constructor(public id?: number, public name?: string) {}
}
