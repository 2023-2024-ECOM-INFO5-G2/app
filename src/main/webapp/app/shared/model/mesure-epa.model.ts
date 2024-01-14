import { type IPatient } from '@/shared/model/patient.model';
import { type IAlerte } from '@/shared/model/alerte.model';

export interface IMesureEPA {
  id?: number;
  valeur?: number;
  date?: Date;
  patient?: IPatient | null;
  alerte?: IAlerte | null;
}

export class MesureEPA implements IMesureEPA {
  constructor(
    public id?: number,
    public valeur?: number,
    public date?: Date,
    public patient?: IPatient | null,
    public alerte?: IAlerte | null,
  ) {}
}
