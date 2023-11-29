import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MesureService from './mesure.service';
import { type IMesure } from '@/shared/model/mesure.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureDetails',
  setup() {
    const mesureService = inject('mesureService', () => new MesureService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mesure: Ref<IMesure> = ref({});

    const retrieveMesure = async mesureId => {
      try {
        const res = await mesureService().find(mesureId);
        mesure.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesureId) {
      retrieveMesure(route.params.mesureId);
    }

    return {
      alertService,
      mesure,

      previousState,
      t$: useI18n().t,
    };
  },
});
