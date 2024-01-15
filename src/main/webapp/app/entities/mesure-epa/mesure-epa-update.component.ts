import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MesureEPAService from './mesure-epa.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IMesureEPA, MesureEPA } from '@/shared/model/mesure-epa.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureEPAUpdate',
  setup() {
    const mesureEPAService = inject('mesureEPAService', () => new MesureEPAService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesureEPA: Ref<IMesureEPA> = ref(new MesureEPA());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMesureEPA = async mesureEPAId => {
      try {
        const res = await mesureEPAService().find(mesureEPAId);
        res.date = new Date(res.date);
        mesureEPA.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesureEPAId) {
      retrieveMesureEPA(route.params.mesureEPAId);
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
      valeur: {
        required: validations.required(t$('entity.validation.required').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
        max: validations.maxValue(t$('entity.validation.max', { max: 10 }).toString(), 10),
      },
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      patient: {},
      alerte: {},
    };
    const v$ = useVuelidate(validationRules, mesureEPA as any);
    v$.value.$validate();

    return {
      mesureEPAService,
      alertService,
      mesureEPA,
      previousState,
      isSaving,
      currentLanguage,
      patients,
      v$,
      ...useDateFormat({ entityRef: mesureEPA }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.mesureEPA.id) {
        this.mesureEPAService()
          .update(this.mesureEPA)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ecom02App.mesureEPA.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mesureEPAService()
          .create(this.mesureEPA)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ecom02App.mesureEPA.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
