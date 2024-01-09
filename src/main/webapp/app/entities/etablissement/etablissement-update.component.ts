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
      nom: {
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 64 }).toString(), 64),
      },
      adresse: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 128 }).toString(), 128),
      },
      ville: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 64 }).toString(), 64),
      },
      codePostal: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 5 }).toString(), 5),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
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
            this.alertService.showInfo(this.t$('ecom02App.etablissement.updated', { param: param.id }));
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
            this.alertService.showSuccess(this.t$('ecom02App.etablissement.created', { param: param.id }).toString());
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
