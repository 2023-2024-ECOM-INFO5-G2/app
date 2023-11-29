import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MedecinService from './medecin.service';
import { type IMedecin } from '@/shared/model/medecin.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MedecinDetails',
  setup() {
    const medecinService = inject('medecinService', () => new MedecinService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const medecin: Ref<IMedecin> = ref({});

    const retrieveMedecin = async medecinId => {
      try {
        const res = await medecinService().find(medecinId);
        medecin.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.medecinId) {
      retrieveMedecin(route.params.medecinId);
    }

    return {
      alertService,
      medecin,

      previousState,
      t$: useI18n().t,
    };
  },
});
