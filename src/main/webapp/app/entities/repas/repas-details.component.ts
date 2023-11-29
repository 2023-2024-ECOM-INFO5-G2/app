import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import RepasService from './repas.service';
import { type IRepas } from '@/shared/model/repas.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'RepasDetails',
  setup() {
    const repasService = inject('repasService', () => new RepasService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const repas: Ref<IRepas> = ref({});

    const retrieveRepas = async repasId => {
      try {
        const res = await repasService().find(repasId);
        repas.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.repasId) {
      retrieveRepas(route.params.repasId);
    }

    return {
      alertService,
      repas,

      previousState,
      t$: useI18n().t,
    };
  },
});
