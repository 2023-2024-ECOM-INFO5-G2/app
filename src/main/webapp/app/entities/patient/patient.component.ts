import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PatientService from './patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Patient',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const patientService = inject('patientService', () => new PatientService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const patients: Ref<IPatient[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

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
        const message = t$('ecom02App.patient.deleted', { param: removeId.value }).toString();
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
      handleSyncList,
      isFetching,
      retrievePatients,
      clear,
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
