import { type IPatient } from '@/shared/model/patient.model';

export interface IMesureEPA {
  id?: number;
  valeur?: number;
  date?: Date;
  patient?: IPatient | null;
}

export class MesureEPA implements IMesureEPA {
  constructor(
    public id?: number,
    public valeur?: number,
    public date?: Date,
    public patient?: IPatient | null,
  ) {}
}
