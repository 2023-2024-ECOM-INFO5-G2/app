import { type IPatient } from '@/shared/model/patient.model';
import { type IMedecin } from '@/shared/model/medecin.model';

export interface IAlerte {
  id?: number;
  description?: string | null;
  date?: Date | null;
  patients?: IPatient[] | null;
  medecins?: IMedecin[] | null;
}

export class Alerte implements IAlerte {
  constructor(
    public id?: number,
    public description?: string | null,
    public date?: Date | null,
    public patients?: IPatient[] | null,
    public medecins?: IMedecin[] | null,
  ) {}
}
