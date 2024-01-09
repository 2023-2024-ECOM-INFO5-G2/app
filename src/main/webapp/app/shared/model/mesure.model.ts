import { type IPatient } from '@/shared/model/patient.model';

export interface IMesure {
  id?: number;
  date?: Date;
  nomValeur?: string;
  valeur?: number;
  patient?: IPatient | null;
}

export class Mesure implements IMesure {
  constructor(
    public id?: number,
    public date?: Date,
    public nomValeur?: string,
    public valeur?: number,
    public patient?: IPatient | null,
  ) {}
}
