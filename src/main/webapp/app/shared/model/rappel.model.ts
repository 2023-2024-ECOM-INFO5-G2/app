import { type IUser } from '@/shared/model/user.model';
import { type IPatient } from '@/shared/model/patient.model';

export interface IRappel {
  id?: number;
  date?: Date;
  echeance?: Date;
  intervaleJours?: number;
  tache?: string;
  feeDansLetang?: boolean;
  users?: IUser[] | null;
  patient?: IPatient | null;
}

export class Rappel implements IRappel {
  constructor(
    public id?: number,
    public date?: Date,
    public echeance?: Date,
    public intervaleJours?: number,
    public tache?: string,
    public feeDansLetang?: boolean,
    public users?: IUser[] | null,
    public patient?: IPatient | null,
  ) {
    this.feeDansLetang = this.feeDansLetang ?? false;
  }
}
