import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import MedecinService from './medecin.service';
import { type IMedecin } from '@/shared/model/medecin.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Medecin',
  setup() {
    const { t: t$ } = useI18n();
    const medecinService = inject('medecinService', () => new MedecinService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const medecins: Ref<IMedecin[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMedecins = async () => {
      isFetching.value = true;
      try {
        const res = await medecinService().retrieve();
        medecins.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMedecins();
    };

    onMounted(async () => {
      await retrieveMedecins();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMedecin) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMedecin = async () => {
      try {
        await medecinService().delete(removeId.value);
        const message = t$('g2EcomApp.medecin.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMedecins();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      medecins,
      handleSyncList,
      isFetching,
      retrieveMedecins,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMedecin,
      t$,
    };
  },
});
