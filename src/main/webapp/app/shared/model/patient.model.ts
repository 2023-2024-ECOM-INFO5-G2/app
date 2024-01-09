import { type IAlerte } from '@/shared/model/alerte.model';
import { type IRappel } from '@/shared/model/rappel.model';
import { type IMesurePoids } from '@/shared/model/mesure-poids.model';
import { type IMesureEPA } from '@/shared/model/mesure-epa.model';
import { type IMesureAlbumine } from '@/shared/model/mesure-albumine.model';
import { type IRepas } from '@/shared/model/repas.model';
import { type IUser } from '@/shared/model/user.model';
import { type IEtablissement } from '@/shared/model/etablissement.model';

export interface IPatient {
  id?: number;
  prenom?: string;
  nom?: string;
  sexe?: string;
  taille?: number;
  dateDeNaissance?: Date;
  numChambre?: number;
  dateArrivee?: Date;
  infoComplementaires?: string | null;
  alertes?: IAlerte[] | null;
  rappels?: IRappel[] | null;
  mesurePoids?: IMesurePoids[] | null;
  mesureEPAS?: IMesureEPA[] | null;
  mesureAlbumines?: IMesureAlbumine[] | null;
  repas?: IRepas[] | null;
  users?: IUser[] | null;
  etablissement?: IEtablissement | null;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public prenom?: string,
    public nom?: string,
    public sexe?: string,
    public taille?: number,
    public dateDeNaissance?: Date,
    public numChambre?: number,
    public dateArrivee?: Date,
    public infoComplementaires?: string | null,
    public alertes?: IAlerte[] | null,
    public rappels?: IRappel[] | null,
    public mesurePoids?: IMesurePoids[] | null,
    public mesureEPAS?: IMesureEPA[] | null,
    public mesureAlbumines?: IMesureAlbumine[] | null,
    public repas?: IRepas[] | null,
    public users?: IUser[] | null,
    public etablissement?: IEtablissement | null,
  ) {}
}
