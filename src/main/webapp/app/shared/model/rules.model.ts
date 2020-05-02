import { IAction } from 'app/shared/model/action.model';

export interface IRules {
  id?: number;
  groupeName?: string;
  actions?: IAction[];
}

export class Rules implements IRules {
  constructor(public id?: number, public groupeName?: string, public actions?: IAction[]) {}
}
