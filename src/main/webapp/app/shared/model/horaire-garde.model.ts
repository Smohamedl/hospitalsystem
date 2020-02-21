export interface IHoraire_garde {
  id?: number;
  start?: number;
  end?: number;
  name?: string;
}

export class Horaire_garde implements IHoraire_garde {
  constructor(public id?: number, public start?: number, public end?: number, public name?: string) {}
}
