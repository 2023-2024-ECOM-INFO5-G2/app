import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Patient = () => import('@/entities/patient/patient.vue');
const PatientUpdate = () => import('@/entities/patient/patient-update.vue');
const PatientDetails = () => import('@/entities/patient/patient-details.vue');

const Etablissement = () => import('@/entities/etablissement/etablissement.vue');
const EtablissementUpdate = () => import('@/entities/etablissement/etablissement-update.vue');
const EtablissementDetails = () => import('@/entities/etablissement/etablissement-details.vue');

const Repas = () => import('@/entities/repas/repas.vue');
const RepasUpdate = () => import('@/entities/repas/repas-update.vue');
const RepasDetails = () => import('@/entities/repas/repas-details.vue');

const Rappel = () => import('@/entities/rappel/rappel.vue');
const RappelUpdate = () => import('@/entities/rappel/rappel-update.vue');
const RappelDetails = () => import('@/entities/rappel/rappel-details.vue');

const Alerte = () => import('@/entities/alerte/alerte.vue');
const AlerteUpdate = () => import('@/entities/alerte/alerte-update.vue');
const AlerteDetails = () => import('@/entities/alerte/alerte-details.vue');

const MesurePoids = () => import('@/entities/mesure-poids/mesure-poids.vue');
const MesurePoidsUpdate = () => import('@/entities/mesure-poids/mesure-poids-update.vue');
const MesurePoidsDetails = () => import('@/entities/mesure-poids/mesure-poids-details.vue');

const MesureEPA = () => import('@/entities/mesure-epa/mesure-epa.vue');
const MesureEPAUpdate = () => import('@/entities/mesure-epa/mesure-epa-update.vue');
const MesureEPADetails = () => import('@/entities/mesure-epa/mesure-epa-details.vue');

const MesureAlbumine = () => import('@/entities/mesure-albumine/mesure-albumine.vue');
const MesureAlbumineUpdate = () => import('@/entities/mesure-albumine/mesure-albumine-update.vue');
const MesureAlbumineDetails = () => import('@/entities/mesure-albumine/mesure-albumine-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'patient',
      name: 'Patient',
      component: Patient,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'patient/new',
      name: 'PatientCreate',
      component: PatientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'patient/:patientId/edit',
      name: 'PatientEdit',
      component: PatientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'patient/:patientId/view',
      name: 'PatientView',
      component: PatientDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'etablissement',
      name: 'Etablissement',
      component: Etablissement,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'etablissement/new',
      name: 'EtablissementCreate',
      component: EtablissementUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'etablissement/:etablissementId/edit',
      name: 'EtablissementEdit',
      component: EtablissementUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'etablissement/:etablissementId/view',
      name: 'EtablissementView',
      component: EtablissementDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'repas',
      name: 'Repas',
      component: Repas,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'repas/new',
      name: 'RepasCreate',
      component: RepasUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'repas/:repasId/edit',
      name: 'RepasEdit',
      component: RepasUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'repas/:repasId/view',
      name: 'RepasView',
      component: RepasDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'rappel',
      name: 'Rappel',
      component: Rappel,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'rappel/new',
      name: 'RappelCreate',
      component: RappelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'rappel/:rappelId/edit',
      name: 'RappelEdit',
      component: RappelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'rappel/:rappelId/view',
      name: 'RappelView',
      component: RappelDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte',
      name: 'Alerte',
      component: Alerte,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte/new',
      name: 'AlerteCreate',
      component: AlerteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte/:alerteId/edit',
      name: 'AlerteEdit',
      component: AlerteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alerte/:alerteId/view',
      name: 'AlerteView',
      component: AlerteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-poids',
      name: 'MesurePoids',
      component: MesurePoids,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-poids/new',
      name: 'MesurePoidsCreate',
      component: MesurePoidsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-poids/:mesurePoidsId/edit',
      name: 'MesurePoidsEdit',
      component: MesurePoidsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-poids/:mesurePoidsId/view',
      name: 'MesurePoidsView',
      component: MesurePoidsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-epa',
      name: 'MesureEPA',
      component: MesureEPA,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-epa/new',
      name: 'MesureEPACreate',
      component: MesureEPAUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-epa/:mesureEPAId/edit',
      name: 'MesureEPAEdit',
      component: MesureEPAUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-epa/:mesureEPAId/view',
      name: 'MesureEPAView',
      component: MesureEPADetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-albumine',
      name: 'MesureAlbumine',
      component: MesureAlbumine,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-albumine/new',
      name: 'MesureAlbumineCreate',
      component: MesureAlbumineUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-albumine/:mesureAlbumineId/edit',
      name: 'MesureAlbumineEdit',
      component: MesureAlbumineUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure-albumine/:mesureAlbumineId/view',
      name: 'MesureAlbumineView',
      component: MesureAlbumineDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
