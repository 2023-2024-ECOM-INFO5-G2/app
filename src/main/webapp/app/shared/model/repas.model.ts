import { type IPatient } from '@/shared/model/patient.model';

export interface IRepas {
  id?: number;
  nom?: string;
  description?: string;
  apportCalorique?: number;
  patients?: IPatient[] | null;
}

export class Repas implements IRepas {
  constructor(
    public id?: number,
    public nom?: string,
    public description?: string,
    public apportCalorique?: number,
    public patients?: IPatient[] | null,
  ) {}
}
