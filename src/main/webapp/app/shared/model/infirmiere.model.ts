import { type IUser } from '@/shared/model/user.model';
import { type IPatient } from '@/shared/model/patient.model';

export interface IInfirmiere {
  id?: number;
  users?: IUser[] | null;
  patients?: IPatient[] | null;
}

export class Infirmiere implements IInfirmiere {
  constructor(
    public id?: number,
    public users?: IUser[] | null,
    public patients?: IPatient[] | null,
  ) {}
}
