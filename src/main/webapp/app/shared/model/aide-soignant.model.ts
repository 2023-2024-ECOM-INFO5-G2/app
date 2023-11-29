import { type IUser } from '@/shared/model/user.model';
import { type IPatient } from '@/shared/model/patient.model';

export interface IAideSoignant {
  id?: number;
  users?: IUser[] | null;
  patients?: IPatient[] | null;
}

export class AideSoignant implements IAideSoignant {
  constructor(
    public id?: number,
    public users?: IUser[] | null,
    public patients?: IPatient[] | null,
  ) {}
}
