import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AlerteService from './alerte.service';
import { useDateFormat } from '@/shared/composables';
import { type IAlerte } from '@/shared/model/alerte.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AlerteDetails',
  setup() {
    const dateFormat = useDateFormat();
    const alerteService = inject('alerteService', () => new AlerteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const alerte: Ref<IAlerte> = ref({});

    const retrieveAlerte = async alerteId => {
      try {
        const res = await alerteService().find(alerteId);
        alerte.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.alerteId) {
      retrieveAlerte(route.params.alerteId);
    }

    return {
      ...dateFormat,
      alertService,
      alerte,

      previousState,
      t$: useI18n().t,
    };
  },
});
