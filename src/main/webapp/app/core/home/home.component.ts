import { type ComputedRef, defineComponent, inject, onMounted, type Ref, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import EtablissementService from '../../entities/etablissement/etablissement.service';
import type { IEtablissement } from '../../shared/model/etablissement.model';
import PatientService from '../../entities/patient/patient.service';
import RappelService from '../../entities/rappel/rappel.service';
import AlerteService from '../../entities/alerte/alerte.service';
import { type IPatient } from '../../shared/model/patient.model';
import { type IRappel } from '../../shared/model/rappel.model';
import { useAlertService } from '../../shared/alert/alert.service';

import type LoginService from '@/account/login.service';
import type { IAlerte } from '../../shared/model/alerte.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const loginService = inject<LoginService>('loginService');
    const etablissementService = inject('etablissementService', () => new EtablissementService());
    const selectedetablissement = ref({});
    const etablissements: Ref<IEtablissement[]> = ref([]);
    const patientService = inject('patientService', () => new PatientService());
    const rappelService = inject('rappelService', () => new RappelService());
    const alerteService = inject('alerteService', () => new AlerteService());
    const patients: Ref<IPatient[]> = ref([]);

    const alertService = inject('alertService', () => useAlertService(), true);
    const isFetching = ref(false);
    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    const instructionDesc: Ref<string> = ref('');
    const instructionEcheance: Ref<string> = ref('');
    const instructionPatient: Ref<Object> = ref(null);
    const instructionInterv: Ref<number> = ref(null);
    const instructionOptions: Ref<Array<Object>> = ref([
      {
        value: null,
        text: 'Selectionner un patient',
      },
    ]);

    const alertes: Ref<Array<IAlerte>> = ref([]);

    const retrieveEtablissements = async () => {
      isFetching.value = true;
      try {
        const res = await etablissementService().retrieve();
        etablissements.value = res.data;
        selectedetablissement.value = etablissements.value[0];
      } catch (err: any) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const retrievePatients = async () => {
      isFetching.value = true;
      try {
        const res = await patientService().retrieve();
        patients.value = res.data;
        for (const patient of patients.value) {
          instructionOptions.value.push({
            value: patient.id,
            text: patient.prenom + ' ' + patient.nom,
          });
        }
      } catch (err: any) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const retrieveAlertes = async () => {
      try {
        const res = await alerteService().retrieve();
        alertes.value = res.data;
      } catch (err: any) {
        alertService.showHttpError(err.response);
      }
    };

    onMounted(async () => {
      await retrieveEtablissements(); //FIXME : à supprimer ???
      await retrievePatients();
      await retrieveAlertes();
    });

    const openLogin = () => {
      loginService.openLogin();
    };

    const addInstruction = async () => {
      const rappel: IRappel = {
        date: new Date().toISOString(),
        echeance: new Date(instructionEcheance.value).toISOString(),
        intervaleJours: instructionInterv.value,
        tache: instructionDesc.value,
        feeDansLetang: true,
        patient: { id: instructionPatient.value },
      };
      try {
        await rappelService().create(rappel);
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const selectEtablissement = (etablissement: IEtablissement) => {
      selectedetablissement.value = etablissement;
    };

    const getAlertesCount = () => {
      const patientsForEta = patients.value?.filter(p => p.etablissement && p.etablissement.id === selectedetablissement.value.id);
      const alertesForEta = alertes.value?.filter(a => a.patient?.id in patientsForEta.map(p => p.id));
      return (
        alertesForEta.length +
        ' alertes pour ' +
        alertesForEta.reduce((acc, item) => acc.add(item.patient.id), new Set()).size +
        ' patient(s)'
      );
    };

    return {
      authenticated,
      username,
      etablissements,
      isFetching,
      alertes,
      retrievePatients,
      patients,
      selectedetablissement,
      selectEtablissement,
      openLogin,
      instructionDesc,
      instructionPatient,
      instructionOptions,
      instructionEcheance,
      instructionInterv,
      addInstruction,
      getAlertesCount,
      t$: useI18n().t,
    };
  },
});
