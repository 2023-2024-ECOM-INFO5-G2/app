import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import InfirmiereService from './infirmiere.service';
import { type IInfirmiere } from '@/shared/model/infirmiere.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfirmiereDetails',
  setup() {
    const infirmiereService = inject('infirmiereService', () => new InfirmiereService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const infirmiere: Ref<IInfirmiere> = ref({});

    const retrieveInfirmiere = async infirmiereId => {
      try {
        const res = await infirmiereService().find(infirmiereId);
        infirmiere.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.infirmiereId) {
      retrieveInfirmiere(route.params.infirmiereId);
    }

    return {
      alertService,
      infirmiere,

      previousState,
      t$: useI18n().t,
    };
  },
});
