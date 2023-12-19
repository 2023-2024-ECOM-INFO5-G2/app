import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MesureAlbumineService from './mesure-albumine.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import { type IMesureAlbumine, MesureAlbumine } from '@/shared/model/mesure-albumine.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MesureAlbumineUpdate',
  setup() {
    const mesureAlbumineService = inject('mesureAlbumineService', () => new MesureAlbumineService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mesureAlbumine: Ref<IMesureAlbumine> = ref(new MesureAlbumine());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMesureAlbumine = async mesureAlbumineId => {
      try {
        const res = await mesureAlbumineService().find(mesureAlbumineId);
        res.date = new Date(res.date);
        mesureAlbumine.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mesureAlbumineId) {
      retrieveMesureAlbumine(route.params.mesureAlbumineId);
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
        max: validations.maxValue(t$('entity.validation.max', { max: 100 }).toString(), 100),
      },
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      patient: {},
    };
    const v$ = useVuelidate(validationRules, mesureAlbumine as any);
    v$.value.$validate();

    return {
      mesureAlbumineService,
      alertService,
      mesureAlbumine,
      previousState,
      isSaving,
      currentLanguage,
      patients,
      v$,
      ...useDateFormat({ entityRef: mesureAlbumine }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.mesureAlbumine.id) {
        this.mesureAlbumineService()
          .update(this.mesureAlbumine)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ecom02App.mesureAlbumine.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mesureAlbumineService()
          .create(this.mesureAlbumine)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ecom02App.mesureAlbumine.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
