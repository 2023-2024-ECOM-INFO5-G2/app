import { ref } from 'vue';
import { type ComputedRef, defineComponent, inject, type Ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import EtablissementService from '../../entities/etablissement/etablissement.service';
import type { IEtablissement } from '../../shared/model/etablissement.model';
import { useAlertService } from '../../shared/alert/alert.service';

import type LoginService from '@/account/login.service';
import axios from 'axios';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const loginService = inject<LoginService>('loginService');
    const etablissementService = inject('etablissementService', () => new EtablissementService());
    const selectedetablissement = ref({});
    const etablissements: Ref<IEtablissement[]> = ref([]);
    const alertService = inject('alertService', () => useAlertService(), true);
    const isFetching = ref(false);
    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    const retrieveEtablissements = async () => {
      isFetching.value = true;
      try {
        const res = await etablissementService().retrieve();
        etablissements.value = res.data;
        selectedetablissement.value = etablissements.value[0];
      } catch (err: any) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    onMounted(async () => {
      await retrieveEtablissements(); //FIXME : à supprimer ???
    });

    const etablissementNames = ref<string[]>([]);

    const openLogin = () => {
      loginService.openLogin();
    };

    const getEtablissementNames = async () => {
      try {
        const response = await axios.get('/api/etablissements');
        const etablissements = response.data;
        etablissementNames.value = etablissements.map((etablissement: any) => etablissement.name);
      } catch (error) {
        console.error('Failed to fetch etablissement names:', error);
      }
    };

    const selectEtablissement = (etablissement: IEtablissement) => {
      selectedetablissement.value = etablissement;
    };

    const data = () => {
      return {
        items: [
          { age: 40, first_name: 'Dickerson', last_name: 'Macdonald' },
          { age: 21, first_name: 'Larsen', last_name: 'Shaw' },
          {
            age: 89,
            first_name: 'Geneva',
            last_name: 'Wilson',
            _rowVariant: 'danger',
          },
          {
            age: 40,
            first_name: 'Thor',
            last_name: 'MacDonald',
            _cellVariants: { age: 'info', first_name: 'warning' },
          },
          { age: 29, first_name: 'Dick', last_name: 'Dunlap' },
        ],
      };
    };

    return {
      authenticated,
      username,
      etablissements,
      isFetching,
      selectedetablissement,
      selectEtablissement,
      openLogin,
      t$: useI18n().t,
    };
  },
});
