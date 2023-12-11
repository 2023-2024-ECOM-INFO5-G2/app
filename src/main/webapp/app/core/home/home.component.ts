import { ref } from 'vue';
import { type ComputedRef, defineComponent, inject } from 'vue';
import { useI18n } from 'vue-i18n';

import type LoginService from '@/account/login.service';
import axios from 'axios';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const loginService = inject<LoginService>('loginService');

    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

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
      etablissementNames,
      data,
      openLogin,
      t$: useI18n().t,
      getEtablissementNames,
    };
  },
});
