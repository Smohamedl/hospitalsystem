import { Moment } from 'moment';
import { User } from 'app/core/user/user.model';

export interface ISession {
  id?: number;
  totalCash?: number;
  totalPC?: number;
  total?: number;
  totalCheck?: number;
  created_by?: string;
  created_date?: Moment;
  jhi_user?: User;
}

export class Session implements ISession {
  constructor(
    public id?: number,
    public totalCash?: number,
    public totalPC?: number,
    public total?: number,
    public totalCheck?: number,
    public created_by?: string,
    public created_date?: Moment,
    public jhi_user?: User
  ) {}
}
