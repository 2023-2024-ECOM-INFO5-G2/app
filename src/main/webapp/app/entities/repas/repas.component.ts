import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import RepasService from './repas.service';
import { type IRepas } from '@/shared/model/repas.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Repas',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const repasService = inject('repasService', () => new RepasService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const repas: Ref<IRepas[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveRepass = async () => {
      isFetching.value = true;
      try {
        const res = await repasService().retrieve();
        repas.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveRepass();
    };

    onMounted(async () => {
      await retrieveRepass();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IRepas) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeRepas = async () => {
      try {
        await repasService().delete(removeId.value);
        const message = t$('g2ecomApp.repas.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveRepass();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      repas,
      handleSyncList,
      isFetching,
      retrieveRepass,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeRepas,
      t$,
    };
  },
});
