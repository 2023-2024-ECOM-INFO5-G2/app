import { type IPatient } from '@/shared/model/patient.model';
import { type IUser } from '@/shared/model/user.model';

export interface IEtablissement {
  id?: number;
  nom?: string | null;
  adresse?: string;
  ville?: string;
  codePostal?: number;
  patients?: IPatient[] | null;
  users?: IUser[] | null;
}

export class Etablissement implements IEtablissement {
  constructor(
    public id?: number,
    public nom?: string | null,
    public adresse?: string,
    public ville?: string,
    public codePostal?: number,
    public patients?: IPatient[] | null,
    public users?: IUser[] | null,
  ) {}
}
