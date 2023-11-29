import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import MesureService from './mesure.service';
import { type IMesure } from '@/shared/model/mesure.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Mesure',
  setup() {
    const { t: t$ } = useI18n();
    const mesureService = inject('mesureService', () => new MesureService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesures: Ref<IMesure[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMesures = async () => {
      isFetching.value = true;
      try {
        const res = await mesureService().retrieve();
        mesures.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMesures();
    };

    onMounted(async () => {
      await retrieveMesures();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMesure) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMesure = async () => {
      try {
        await mesureService().delete(removeId.value);
        const message = t$('g2EcomApp.mesure.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMesures();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      mesures,
      handleSyncList,
      isFetching,
      retrieveMesures,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMesure,
      t$,
    };
  },
});
