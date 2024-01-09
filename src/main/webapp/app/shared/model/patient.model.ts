import { type IMesure } from '@/shared/model/mesure.model';
import { type IRappel } from '@/shared/model/rappel.model';
import { type IMedecin } from '@/shared/model/medecin.model';
import { type IEtablissement } from '@/shared/model/etablissement.model';
import { type IAideSoignant } from '@/shared/model/aide-soignant.model';
import { type IInfirmiere } from '@/shared/model/infirmiere.model';
import { type IRepas } from '@/shared/model/repas.model';
import { type IAlerte } from '@/shared/model/alerte.model';

export interface IPatient {
  id?: number;
  prenom?: string;
  nom?: string;
  sexe?: string;
  dateDeNaissance?: Date;
  numChambre?: number;
  taille?: number | null;
  dateArrivee?: Date;
  infoComplementaires?: string | null;
  mesures?: IMesure[] | null;
  rappel?: IRappel | null;
  medecin?: IMedecin | null;
  etablissement?: IEtablissement | null;
  aideSoignants?: IAideSoignant[] | null;
  infirmieres?: IInfirmiere[] | null;
  repas?: IRepas[] | null;
  alertes?: IAlerte[] | null;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public prenom?: string,
    public nom?: string,
    public sexe?: string,
    public dateDeNaissance?: Date,
    public numChambre?: number,
    public taille?: number | null,
    public dateArrivee?: Date,
    public infoComplementaires?: string | null,
    public mesures?: IMesure[] | null,
    public rappel?: IRappel | null,
    public medecin?: IMedecin | null,
    public etablissement?: IEtablissement | null,
    public aideSoignants?: IAideSoignant[] | null,
    public infirmieres?: IInfirmiere[] | null,
    public repas?: IRepas[] | null,
    public alertes?: IAlerte[] | null,
  ) {}
}
