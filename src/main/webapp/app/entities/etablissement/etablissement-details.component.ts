import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import EtablissementService from './etablissement.service';
import { type IEtablissement } from '@/shared/model/etablissement.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EtablissementDetails',
  setup() {
    const etablissementService = inject('etablissementService', () => new EtablissementService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const etablissement: Ref<IEtablissement> = ref({});

    const retrieveEtablissement = async etablissementId => {
      try {
        const res = await etablissementService().find(etablissementId);
        etablissement.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.etablissementId) {
      retrieveEtablissement(route.params.etablissementId);
    }

    return {
      alertService,
      etablissement,

      previousState,
      t$: useI18n().t,
    };
  },
});
