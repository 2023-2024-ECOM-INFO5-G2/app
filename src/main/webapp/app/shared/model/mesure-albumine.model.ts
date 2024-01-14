import { type IPatient } from '@/shared/model/patient.model';
import { type IAlerte } from '@/shared/model/alerte.model';

export interface IMesureAlbumine {
  id?: number;
  valeur?: number;
  date?: Date;
  patient?: IPatient | null;
  alerte?: IAlerte | null;
}

export class MesureAlbumine implements IMesureAlbumine {
  constructor(
    public id?: number,
    public valeur?: number,
    public date?: Date,
    public patient?: IPatient | null,
    public alerte?: IAlerte | null,
  ) {}
}
