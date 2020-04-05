export interface IProvidedr {
  id?: number;
  name?: string;
  tel?: string;
  adress?: string;
}

export class Providedr implements IProvidedr {
  constructor(public id?: number, public name?: string, public tel?: string, public adress?: string) {}
}
