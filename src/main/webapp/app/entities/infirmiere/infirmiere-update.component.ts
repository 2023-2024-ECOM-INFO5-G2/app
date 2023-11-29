import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import InfirmiereService from './infirmiere.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IInfirmiere, Infirmiere } from '@/shared/model/infirmiere.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfirmiereUpdate',
  setup() {
    const infirmiereService = inject('infirmiereService', () => new InfirmiereService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const infirmiere: Ref<IInfirmiere> = ref(new Infirmiere());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
      patientService()
        .retrieve()
        .then(res => {
          patients.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      users: {},
      patients: {},
    };
    const v$ = useVuelidate(validationRules, infirmiere as any);
    v$.value.$validate();

    return {
      infirmiereService,
      alertService,
      infirmiere,
      previousState,
      isSaving,
      currentLanguage,
      users,
      patients,
      v$,
      t$,
    };
  },
  created(): void {
    this.infirmiere.users = [];
    this.infirmiere.patients = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.infirmiere.id) {
        this.infirmiereService()
          .update(this.infirmiere)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('g2EcomApp.infirmiere.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.infirmiereService()
          .create(this.infirmiere)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('g2EcomApp.infirmiere.created', { param: param.id }).toString());
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
