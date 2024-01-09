import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MedecinService from './medecin.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import AlerteService from '@/entities/alerte/alerte.service';
import { type IAlerte } from '@/shared/model/alerte.model';
import { type IMedecin, Medecin } from '@/shared/model/medecin.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MedecinUpdate',
  setup() {
    const medecinService = inject('medecinService', () => new MedecinService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const medecin: Ref<IMedecin> = ref(new Medecin());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);

    const alerteService = inject('alerteService', () => new AlerteService());

    const alertes: Ref<IAlerte[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
      alerteService()
        .retrieve()
        .then(res => {
          alertes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      patients: {},
      users: {},
      alertes: {},
      rappels: {},
    };
    const v$ = useVuelidate(validationRules, medecin as any);
    v$.value.$validate();

    return {
      medecinService,
      alertService,
      medecin,
      previousState,
      isSaving,
      currentLanguage,
      users,
      alertes,
      v$,
      t$,
    };
  },
  created(): void {
    this.medecin.users = [];
    this.medecin.alertes = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.medecin.id) {
        this.medecinService()
          .update(this.medecin)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('g2EcomApp.medecin.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.medecinService()
          .create(this.medecin)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('g2EcomApp.medecin.created', { param: param.id }).toString());
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
