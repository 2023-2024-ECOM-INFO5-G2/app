import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import MesureAlbumineService from './mesure-albumine.service';
import { type IMesureAlbumine } from '@/shared/model/mesure-albumine.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureAlbumine',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const mesureAlbumineService = inject('mesureAlbumineService', () => new MesureAlbumineService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesureAlbumines: Ref<IMesureAlbumine[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMesureAlbumines = async () => {
      isFetching.value = true;
      try {
        const res = await mesureAlbumineService().retrieve();
        mesureAlbumines.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMesureAlbumines();
    };

    onMounted(async () => {
      await retrieveMesureAlbumines();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMesureAlbumine) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMesureAlbumine = async () => {
      try {
        await mesureAlbumineService().delete(removeId.value);
        const message = t$('g2ecomApp.mesureAlbumine.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMesureAlbumines();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      mesureAlbumines,
      handleSyncList,
      isFetching,
      retrieveMesureAlbumines,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMesureAlbumine,
      t$,
    };
  },
});
