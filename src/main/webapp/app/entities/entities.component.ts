import { defineComponent, provide } from 'vue';

import PatientService from './patient/patient.service';
import AideSoignantService from './aide-soignant/aide-soignant.service';
import InfirmiereService from './infirmiere/infirmiere.service';
import MedecinService from './medecin/medecin.service';
import EtablissementService from './etablissement/etablissement.service';
import RepasService from './repas/repas.service';
import RappelService from './rappel/rappel.service';
import AlerteService from './alerte/alerte.service';
import MesureService from './mesure/mesure.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('patientService', () => new PatientService());
    provide('aideSoignantService', () => new AideSoignantService());
    provide('infirmiereService', () => new InfirmiereService());
    provide('medecinService', () => new MedecinService());
    provide('etablissementService', () => new EtablissementService());
    provide('repasService', () => new RepasService());
    provide('rappelService', () => new RappelService());
    provide('alerteService', () => new AlerteService());
    provide('mesureService', () => new MesureService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
