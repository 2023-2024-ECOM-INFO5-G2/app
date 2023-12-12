import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PatientService from './patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import EtablissementService from '../etablissement/etablissement.service';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';
import type { IEtablissement } from 'shared/model/etablissement.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Patient',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const patientService = inject('patientService', () => new PatientService());
    const etablissementService = inject('etablissementService', () => new EtablissementService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const selectedetablissement = ref({});

    const patients: Ref<IPatient[]> = ref([]);
    const etablissements: Ref<IEtablissement[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

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
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePatients();
    };

    onMounted(async () => {
      await retrievePatients();
      await retrieveEtablissements(); //FIXME : à supprimer ???
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPatient) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePatient = async () => {
      try {
        await patientService().delete(removeId.value);
        const message = t$('g2ecomApp.patient.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePatients();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      patients,
      etablissements,
      handleSyncList,
      isFetching,
      retrievePatients,
      clear,
      selectedetablissement,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePatient,
      t$,
    };
  },
});
