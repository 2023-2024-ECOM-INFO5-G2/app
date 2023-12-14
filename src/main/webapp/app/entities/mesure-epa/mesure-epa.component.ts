import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import MesureEPAService from './mesure-epa.service';
import { type IMesureEPA } from '@/shared/model/mesure-epa.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureEPA',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const mesureEPAService = inject('mesureEPAService', () => new MesureEPAService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesureEPAS: Ref<IMesureEPA[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMesureEPAs = async () => {
      isFetching.value = true;
      try {
        const res = await mesureEPAService().retrieve();
        mesureEPAS.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMesureEPAs();
    };

    onMounted(async () => {
      await retrieveMesureEPAs();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMesureEPA) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMesureEPA = async () => {
      try {
        await mesureEPAService().delete(removeId.value);
        const message = t$('g2ecomApp.mesureEPA.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMesureEPAs();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      mesureEPAS,
      handleSyncList,
      isFetching,
      retrieveMesureEPAs,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMesureEPA,
      t$,
    };
  },
});
