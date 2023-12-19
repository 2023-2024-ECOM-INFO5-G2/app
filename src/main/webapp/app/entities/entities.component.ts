import { defineComponent, provide } from 'vue';

import PatientService from './patient/patient.service';
import EtablissementService from './etablissement/etablissement.service';
import RepasService from './repas/repas.service';
import RappelService from './rappel/rappel.service';
import AlerteService from './alerte/alerte.service';
import MesurePoidsService from './mesure-poids/mesure-poids.service';
import MesureEPAService from './mesure-epa/mesure-epa.service';
import MesureAlbumineService from './mesure-albumine/mesure-albumine.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('patientService', () => new PatientService());
    provide('etablissementService', () => new EtablissementService());
    provide('repasService', () => new RepasService());
    provide('rappelService', () => new RappelService());
    provide('alerteService', () => new AlerteService());
    provide('mesurePoidsService', () => new MesurePoidsService());
    provide('mesureEPAService', () => new MesureEPAService());
    provide('mesureAlbumineService', () => new MesureAlbumineService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
