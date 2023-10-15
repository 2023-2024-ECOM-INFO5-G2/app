import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import RepasService from './repas.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IRepas, Repas } from '@/shared/model/repas.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'RepasUpdate',
  setup() {
    const repasService = inject('repasService', () => new RepasService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const repas: Ref<IRepas> = ref(new Repas());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRepas = async repasId => {
      try {
        const res = await repasService().find(repasId);
        repas.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.repasId) {
      retrieveRepas(route.params.repasId);
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
      idRepas: {
        required: validations.required(t$('entity.validation.required').toString()),
        numeric: validations.numeric(t$('entity.validation.number').toString()),
      },
      nom: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      description: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      apportCalorique: {
        required: validations.required(t$('entity.validation.required').toString()),
        numeric: validations.numeric(t$('entity.validation.number').toString()),
      },
      idRepas: {},
    };
    const v$ = useVuelidate(validationRules, repas as any);
    v$.value.$validate();

    return {
      repasService,
      alertService,
      repas,
      previousState,
      isSaving,
      currentLanguage,
      patients,
      v$,
      t$,
    };
  },
  created(): void {
    this.repas.idRepas = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.repas.id) {
        this.repasService()
          .update(this.repas)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ecom02App.repas.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.repasService()
          .create(this.repas)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ecom02App.repas.created', { param: param.id }).toString());
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
