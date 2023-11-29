import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Patient = () => import('@/entities/patient/patient.vue');
const PatientUpdate = () => import('@/entities/patient/patient-update.vue');
const PatientDetails = () => import('@/entities/patient/patient-details.vue');

const AideSoignant = () => import('@/entities/aide-soignant/aide-soignant.vue');
const AideSoignantUpdate = () => import('@/entities/aide-soignant/aide-soignant-update.vue');
const AideSoignantDetails = () => import('@/entities/aide-soignant/aide-soignant-details.vue');

const Infirmiere = () => import('@/entities/infirmiere/infirmiere.vue');
const InfirmiereUpdate = () => import('@/entities/infirmiere/infirmiere-update.vue');
const InfirmiereDetails = () => import('@/entities/infirmiere/infirmiere-details.vue');

const Medecin = () => import('@/entities/medecin/medecin.vue');
const MedecinUpdate = () => import('@/entities/medecin/medecin-update.vue');
const MedecinDetails = () => import('@/entities/medecin/medecin-details.vue');

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

const Mesure = () => import('@/entities/mesure/mesure.vue');
const MesureUpdate = () => import('@/entities/mesure/mesure-update.vue');
const MesureDetails = () => import('@/entities/mesure/mesure-details.vue');

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
      path: 'aide-soignant',
      name: 'AideSoignant',
      component: AideSoignant,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'aide-soignant/new',
      name: 'AideSoignantCreate',
      component: AideSoignantUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'aide-soignant/:aideSoignantId/edit',
      name: 'AideSoignantEdit',
      component: AideSoignantUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'aide-soignant/:aideSoignantId/view',
      name: 'AideSoignantView',
      component: AideSoignantDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'infirmiere',
      name: 'Infirmiere',
      component: Infirmiere,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'infirmiere/new',
      name: 'InfirmiereCreate',
      component: InfirmiereUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'infirmiere/:infirmiereId/edit',
      name: 'InfirmiereEdit',
      component: InfirmiereUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'infirmiere/:infirmiereId/view',
      name: 'InfirmiereView',
      component: InfirmiereDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medecin',
      name: 'Medecin',
      component: Medecin,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medecin/new',
      name: 'MedecinCreate',
      component: MedecinUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medecin/:medecinId/edit',
      name: 'MedecinEdit',
      component: MedecinUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medecin/:medecinId/view',
      name: 'MedecinView',
      component: MedecinDetails,
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
      path: 'mesure',
      name: 'Mesure',
      component: Mesure,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure/new',
      name: 'MesureCreate',
      component: MesureUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure/:mesureId/edit',
      name: 'MesureEdit',
      component: MesureUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mesure/:mesureId/view',
      name: 'MesureView',
      component: MesureDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
