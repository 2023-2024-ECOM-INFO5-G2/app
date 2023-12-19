import { type IPatient } from '@/shared/model/patient.model';

export interface IMesurePoids {
  id?: number;
  valeur?: number;
  date?: Date;
  patient?: IPatient | null;
}

export class MesurePoids implements IMesurePoids {
  constructor(
    public id?: number,
    public valeur?: number,
    public date?: Date,
    public patient?: IPatient | null,
  ) {}
}
