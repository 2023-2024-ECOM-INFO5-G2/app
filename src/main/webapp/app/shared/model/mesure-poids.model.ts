import { type IPatient } from '@/shared/model/patient.model';
import { type IAlerte } from '@/shared/model/alerte.model';

export interface IMesurePoids {
  id?: number;
  valeur?: number;
  date?: Date;
  patient?: IPatient | null;
  alertes?: IAlerte[] | null;
}

export class MesurePoids implements IMesurePoids {
  constructor(
    public id?: number,
    public valeur?: number,
    public date?: Date,
    public patient?: IPatient | null,
    public alertes?: IAlerte[] | null,
  ) {}
}
