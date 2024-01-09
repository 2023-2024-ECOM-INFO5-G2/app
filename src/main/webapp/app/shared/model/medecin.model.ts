import { type IPatient } from '@/shared/model/patient.model';
import { type IUser } from '@/shared/model/user.model';
import { type IAlerte } from '@/shared/model/alerte.model';
import { type IRappel } from '@/shared/model/rappel.model';

export interface IMedecin {
  id?: number;
  patients?: IPatient[] | null;
  users?: IUser[] | null;
  alertes?: IAlerte[] | null;
  rappels?: IRappel[] | null;
}

export class Medecin implements IMedecin {
  constructor(
    public id?: number,
    public patients?: IPatient[] | null,
    public users?: IUser[] | null,
    public alertes?: IAlerte[] | null,
    public rappels?: IRappel[] | null,
  ) {}
}
