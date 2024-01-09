import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MesurePoidsService from './mesure-poids.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IMesurePoids, MesurePoids } from '@/shared/model/mesure-poids.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesurePoidsUpdate',
  setup() {
    const mesurePoidsService = inject('mesurePoidsService', () => new MesurePoidsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesurePoids: Ref<IMesurePoids> = ref(new MesurePoids());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMesurePoids = async mesurePoidsId => {
      try {
        const res = await mesurePoidsService().find(mesurePoidsId);
        res.date = new Date(res.date);
        mesurePoids.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesurePoidsId) {
      retrieveMesurePoids(route.params.mesurePoidsId);
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
        min: validations.minValue(t$('entity.validation.min', { min: 2 }).toString(), 2),
        max: validations.maxValue(t$('entity.validation.max', { max: 635 }).toString(), 635),
      },
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      patient: {},
    };
    const v$ = useVuelidate(validationRules, mesurePoids as any);
    v$.value.$validate();

    return {
      mesurePoidsService,
      alertService,
      mesurePoids,
      previousState,
      isSaving,
      currentLanguage,
      patients,
      v$,
      ...useDateFormat({ entityRef: mesurePoids }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.mesurePoids.id) {
        this.mesurePoidsService()
          .update(this.mesurePoids)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ecom02App.mesurePoids.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mesurePoidsService()
          .create(this.mesurePoids)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ecom02App.mesurePoids.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
