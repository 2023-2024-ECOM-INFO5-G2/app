import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MesureAlbumineService from './mesure-albumine.service';
import { useDateFormat } from '@/shared/composables';
import { type IMesureAlbumine } from '@/shared/model/mesure-albumine.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureAlbumineDetails',
  setup() {
    const dateFormat = useDateFormat();
    const mesureAlbumineService = inject('mesureAlbumineService', () => new MesureAlbumineService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mesureAlbumine: Ref<IMesureAlbumine> = ref({});

    const retrieveMesureAlbumine = async mesureAlbumineId => {
      try {
        const res = await mesureAlbumineService().find(mesureAlbumineId);
        mesureAlbumine.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesureAlbumineId) {
      retrieveMesureAlbumine(route.params.mesureAlbumineId);
    }

    return {
      ...dateFormat,
      alertService,
      mesureAlbumine,

      previousState,
      t$: useI18n().t,
    };
  },
});
