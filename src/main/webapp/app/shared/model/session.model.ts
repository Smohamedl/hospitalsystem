import { User } from 'app/core/user/user.model';

export interface ISession {
  id?: number;
  totalCash?: number;
  totalPC?: number;
  total?: number;
  totalCheck?: number;
  user?: User;
  createdDate: Date;
}

export class Session implements ISession {
  constructor(
    public id?: number,
    public totalCash?: number,
    public totalPC?: number,
    public total?: number,
    public totalCheck?: number,
    public user?: User,
    public createdDate?: Date
  ) {}
}
