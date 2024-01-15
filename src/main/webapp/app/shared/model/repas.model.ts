import { type IPatient } from '@/shared/model/patient.model';

export interface IRepas {
  id?: number;
  nom?: string;
  date?: Date;
  apportCalorique?: number | null;
  poidsConsomme?: number | null;
  description?: string | null;
  patient?: IPatient | null;
}

export class Repas implements IRepas {
  constructor(
    public id?: number,
    public nom?: string,
    public date?: Date,
    public apportCalorique?: number | null,
    public poidsConsomme?: number | null,
    public description?: string | null,
    public patient?: IPatient | null,
  ) {}
}
