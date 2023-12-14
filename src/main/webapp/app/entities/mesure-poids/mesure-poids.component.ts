import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import MesurePoidsService from './mesure-poids.service';
import { type IMesurePoids } from '@/shared/model/mesure-poids.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesurePoids',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const mesurePoidsService = inject('mesurePoidsService', () => new MesurePoidsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesurePoids: Ref<IMesurePoids[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMesurePoidss = async () => {
      isFetching.value = true;
      try {
        const res = await mesurePoidsService().retrieve();
        mesurePoids.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMesurePoidss();
    };

    onMounted(async () => {
      await retrieveMesurePoidss();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMesurePoids) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMesurePoids = async () => {
      try {
        await mesurePoidsService().delete(removeId.value);
        const message = t$('g2ecomApp.mesurePoids.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMesurePoidss();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      mesurePoids,
      handleSyncList,
      isFetching,
      retrieveMesurePoidss,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMesurePoids,
      t$,
    };
  },
});
