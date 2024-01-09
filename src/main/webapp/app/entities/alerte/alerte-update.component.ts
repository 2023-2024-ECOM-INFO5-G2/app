import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AlerteService from './alerte.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IAlerte, Alerte } from '@/shared/model/alerte.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AlerteUpdate',
  setup() {
    const alerteService = inject('alerteService', () => new AlerteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const alerte: Ref<IAlerte> = ref(new Alerte());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAlerte = async alerteId => {
      try {
        const res = await alerteService().find(alerteId);
        res.date = new Date(res.date);
        alerte.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.alerteId) {
      retrieveAlerte(route.params.alerteId);
    }

    const initRelationships = () => {
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
      description: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 512 }).toString(), 512),
      },
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      severe: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      patient: {},
    };
    const v$ = useVuelidate(validationRules, alerte as any);
    v$.value.$validate();

    return {
      alerteService,
      alertService,
      alerte,
      previousState,
      isSaving,
      currentLanguage,
      patients,
      v$,
      ...useDateFormat({ entityRef: alerte }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.alerte.id) {
        this.alerteService()
          .update(this.alerte)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ecom02App.alerte.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.alerteService()
          .create(this.alerte)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ecom02App.alerte.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
