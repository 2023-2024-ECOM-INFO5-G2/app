import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MesureEPAService from './mesure-epa.service';
import { useDateFormat } from '@/shared/composables';
import { type IMesureEPA } from '@/shared/model/mesure-epa.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureEPADetails',
  setup() {
    const dateFormat = useDateFormat();
    const mesureEPAService = inject('mesureEPAService', () => new MesureEPAService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mesureEPA: Ref<IMesureEPA> = ref({});

    const retrieveMesureEPA = async mesureEPAId => {
      try {
        const res = await mesureEPAService().find(mesureEPAId);
        mesureEPA.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesureEPAId) {
      retrieveMesureEPA(route.params.mesureEPAId);
    }

    return {
      ...dateFormat,
      alertService,
      mesureEPA,

      previousState,
      t$: useI18n().t,
    };
  },
});
