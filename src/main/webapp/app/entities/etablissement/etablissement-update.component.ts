import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EtablissementService from './etablissement.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import { type IEtablissement, Etablissement } from '@/shared/model/etablissement.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EtablissementUpdate',
  setup() {
    const etablissementService = inject('etablissementService', () => new EtablissementService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const etablissement: Ref<IEtablissement> = ref(new Etablissement());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nom: {},
      adresse: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      ville: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      codePostal: {
        required: validations.required(t$('entity.validation.required').toString()),
        numeric: validations.numeric(t$('entity.validation.number').toString()),
      },
      patients: {},
      users: {},
    };
    const v$ = useVuelidate(validationRules, etablissement as any);
    v$.value.$validate();

    return {
      etablissementService,
      alertService,
      etablissement,
      previousState,
      isSaving,
      currentLanguage,
      users,
      v$,
      t$,
    };
  },
  created(): void {
    this.etablissement.users = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.etablissement.id) {
        this.etablissementService()
          .update(this.etablissement)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('g2EcomApp.etablissement.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.etablissementService()
          .create(this.etablissement)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('g2EcomApp.etablissement.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option): any {
      if (selectedVals) {
        return selectedVals.find(value => option.id === value.id) ?? option;
      }
      return option;
    },
  },
});
