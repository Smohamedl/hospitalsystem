export interface IProvider {
  id?: number;
  name?: string;
  tel?: string;
  adress?: string;
}

export class Provider implements IProvider {
  constructor(public id?: number, public name?: string, public tel?: string, public adress?: string) {}
}
