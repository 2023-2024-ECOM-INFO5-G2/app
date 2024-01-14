import { type IPatient } from '@/shared/model/patient.model';
import { type IMesureEPA } from '@/shared/model/mesure-epa.model';
import { type IMesureAlbumine } from '@/shared/model/mesure-albumine.model';
import { type IMesurePoids } from '@/shared/model/mesure-poids.model';

export interface IAlerte {
  id?: number;
  description?: string;
  date?: Date;
  severe?: boolean;
  code?: number;
  patient?: IPatient | null;
  mesureEPA?: IMesureEPA | null;
  mesureAlbumine?: IMesureAlbumine | null;
  mesurePoids?: IMesurePoids | null;
}

export class Alerte implements IAlerte {
  constructor(
    public id?: number,
    public description?: string,
    public date?: Date,
    public severe?: boolean,
    public code?: number,
    public patient?: IPatient | null,
    public mesureEPA?: IMesureEPA | null,
    public mesureAlbumine?: IMesureAlbumine | null,
    public mesurePoids?: IMesurePoids | null,
  ) {
    this.severe = this.severe ?? false;
  }
}
