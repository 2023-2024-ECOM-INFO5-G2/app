import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AideSoignantService from './aide-soignant.service';
import { type IAideSoignant } from '@/shared/model/aide-soignant.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AideSoignantDetails',
  setup() {
    const aideSoignantService = inject('aideSoignantService', () => new AideSoignantService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const aideSoignant: Ref<IAideSoignant> = ref({});

    const retrieveAideSoignant = async aideSoignantId => {
      try {
        const res = await aideSoignantService().find(aideSoignantId);
        aideSoignant.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.aideSoignantId) {
      retrieveAideSoignant(route.params.aideSoignantId);
    }

    return {
      alertService,
      aideSoignant,

      previousState,
      t$: useI18n().t,
    };
  },
});
