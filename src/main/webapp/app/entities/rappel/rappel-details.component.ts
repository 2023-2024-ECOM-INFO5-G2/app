import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import RappelService from './rappel.service';
import { useDateFormat } from '@/shared/composables';
import { type IRappel } from '@/shared/model/rappel.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'RappelDetails',
  setup() {
    const dateFormat = useDateFormat();
    const rappelService = inject('rappelService', () => new RappelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const rappel: Ref<IRappel> = ref({});

    const retrieveRappel = async rappelId => {
      try {
        const res = await rappelService().find(rappelId);
        rappel.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.rappelId) {
      retrieveRappel(route.params.rappelId);
    }

    return {
      ...dateFormat,
      alertService,
      rappel,

      previousState,
      t$: useI18n().t,
    };
  },
});
