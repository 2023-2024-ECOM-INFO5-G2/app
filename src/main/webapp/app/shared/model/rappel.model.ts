import { type IPatient } from '@/shared/model/patient.model';
import { type IMedecin } from '@/shared/model/medecin.model';

export interface IRappel {
  id?: number;
  frequence?: string;
  echeance?: Date;
  tache?: string;
  patient?: IPatient | null;
  medecins?: IMedecin[] | null;
}

export class Rappel implements IRappel {
  constructor(
    public id?: number,
    public frequence?: string,
    public echeance?: Date,
    public tache?: string,
    public patient?: IPatient | null,
    public medecins?: IMedecin[] | null,
  ) {}
}
