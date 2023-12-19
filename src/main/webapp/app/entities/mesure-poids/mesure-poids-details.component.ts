import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MesurePoidsService from './mesure-poids.service';
import { useDateFormat } from '@/shared/composables';
import { type IMesurePoids } from '@/shared/model/mesure-poids.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesurePoidsDetails',
  setup() {
    const dateFormat = useDateFormat();
    const mesurePoidsService = inject('mesurePoidsService', () => new MesurePoidsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mesurePoids: Ref<IMesurePoids> = ref({});

    const retrieveMesurePoids = async mesurePoidsId => {
      try {
        const res = await mesurePoidsService().find(mesurePoidsId);
        mesurePoids.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesurePoidsId) {
      retrieveMesurePoids(route.params.mesurePoidsId);
    }

    return {
      ...dateFormat,
      alertService,
      mesurePoids,

      previousState,
      t$: useI18n().t,
    };
  },
});
