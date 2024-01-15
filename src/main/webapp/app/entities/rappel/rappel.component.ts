import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import RappelService from './rappel.service';
import { type IRappel } from '@/shared/model/rappel.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Rappel',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const rappelService = inject('rappelService', () => new RappelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const rappels: Ref<IRappel[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveRappels = async () => {
      isFetching.value = true;
      try {
        const res = await rappelService().retrieve();
        rappels.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveRappels();
    };

    onMounted(async () => {
      await retrieveRappels();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IRappel) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeRappel = async () => {
      try {
        await rappelService().delete(removeId.value);
        const message = t$('ecom02App.rappel.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveRappels();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      rappels,
      handleSyncList,
      isFetching,
      retrieveRappels,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeRappel,
      t$,
    };
  },
});
