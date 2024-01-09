import { type IPatient } from '@/shared/model/patient.model';

export interface IMesureAlbumine {
  id?: number;
  valeur?: number;
  date?: Date;
  patient?: IPatient | null;
}

export class MesureAlbumine implements IMesureAlbumine {
  constructor(
    public id?: number,
    public valeur?: number,
    public date?: Date,
    public patient?: IPatient | null,
  ) {}
}
