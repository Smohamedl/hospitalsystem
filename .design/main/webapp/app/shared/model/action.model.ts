export interface IAction {
  id?: number;
}

export class Action implements IAction {
  constructor(public id?: number) {}
}
