import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import RappelService from './rappel.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IRappel, Rappel } from '@/shared/model/rappel.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'RappelUpdate',
  setup() {
    const rappelService = inject('rappelService', () => new RappelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const rappel: Ref<IRappel> = ref(new Rappel());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRappel = async rappelId => {
      try {
        const res = await rappelService().find(rappelId);
        res.date = new Date(res.date);
        rappel.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.rappelId) {
      retrieveRappel(route.params.rappelId);
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
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      frequenceJour: {
        required: validations.required(t$('entity.validation.required').toString()),
        numeric: validations.numeric(t$('entity.validation.number').toString()),
        maxValue: validations.maxValue(t$('entity.validation.max', { min: 120 }).toString(), 120),
        minValue: validations.minValue(t$('entity.validation.min', { min: 1 }).toString(), 1),
      },
      echeance: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      tache: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      users: {},
      patient: {},
    };
    const v$ = useVuelidate(validationRules, rappel as any);
    v$.value.$validate();

    return {
      rappelService,
      alertService,
      rappel,
      previousState,
      isSaving,
      currentLanguage,
      users,
      patients,
      v$,
      ...useDateFormat({ entityRef: rappel }),
      t$,
    };
  },
  created(): void {
    this.rappel.users = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.rappel.id) {
        this.rappelService()
          .update(this.rappel)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('g2ecomApp.rappel.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.rappelService()
          .create(this.rappel)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('g2ecomApp.rappel.created', { param: param.id }).toString());
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
