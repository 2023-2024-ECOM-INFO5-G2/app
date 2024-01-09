import { ref } from 'vue';
import { type ComputedRef, defineComponent, inject, type Ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import EtablissementService from '../../entities/etablissement/etablissement.service';
import type { IEtablissement } from '../../shared/model/etablissement.model';
import PatientService from '../../entities/patient/patient.service';
import { type IPatient } from '../../shared/model/patient.model';
import { useAlertService } from '../../shared/alert/alert.service';

import type LoginService from '@/account/login.service';
import axios from 'axios';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const loginService = inject<LoginService>('loginService');
    const etablissementService = inject('etablissementService', () => new EtablissementService());
    const selectedetablissement = ref({});
    const etablissements: Ref<IEtablissement[]> = ref([]);
    const patientService = inject('patientService', () => new PatientService());
    const patients: Ref<IPatient[]> = ref([]);

    const alertService = inject('alertService', () => useAlertService(), true);
    const isFetching = ref(false);
    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

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
      } catch (err: any) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    onMounted(async () => {
      await retrieveEtablissements(); //FIXME : à supprimer ???
      await retrievePatients();
    });

    const openLogin = () => {
      loginService.openLogin();
    };

    const selectEtablissement = (etablissement: IEtablissement) => {
      selectedetablissement.value = etablissement;
    };

    return {
      authenticated,
      username,
      etablissements,
      isFetching,
      retrievePatients,
      patients,
      selectedetablissement,
      selectEtablissement,
      openLogin,
      t$: useI18n().t,
    };
  },
});
