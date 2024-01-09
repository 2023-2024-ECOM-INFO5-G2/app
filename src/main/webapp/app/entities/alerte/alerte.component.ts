import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import AlerteService from './alerte.service';
import { type IAlerte } from '@/shared/model/alerte.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Alerte',
  setup() {
    const { t: t$ } = useI18n();
    const alerteService = inject('alerteService', () => new AlerteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const alertes: Ref<IAlerte[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAlertes = async () => {
      isFetching.value = true;
      try {
        const res = await alerteService().retrieve();
        alertes.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAlertes();
    };

    onMounted(async () => {
      await retrieveAlertes();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAlerte) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAlerte = async () => {
      try {
        await alerteService().delete(removeId.value);
        const message = t$('g2EcomApp.alerte.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAlertes();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      alertes,
      handleSyncList,
      isFetching,
      retrieveAlertes,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAlerte,
      t$,
    };
  },
});
