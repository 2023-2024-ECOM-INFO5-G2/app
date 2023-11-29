import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MesureService from './mesure.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IMesure, Mesure } from '@/shared/model/mesure.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureUpdate',
  setup() {
    const mesureService = inject('mesureService', () => new MesureService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesure: Ref<IMesure> = ref(new Mesure());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMesure = async mesureId => {
      try {
        const res = await mesureService().find(mesureId);
        mesure.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesureId) {
      retrieveMesure(route.params.mesureId);
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
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      nomValeur: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      valeur: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      patient: {},
    };
    const v$ = useVuelidate(validationRules, mesure as any);
    v$.value.$validate();

    return {
      mesureService,
      alertService,
      mesure,
      previousState,
      isSaving,
      currentLanguage,
      patients,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.mesure.id) {
        this.mesureService()
          .update(this.mesure)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('g2EcomApp.mesure.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mesureService()
          .create(this.mesure)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('g2EcomApp.mesure.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
