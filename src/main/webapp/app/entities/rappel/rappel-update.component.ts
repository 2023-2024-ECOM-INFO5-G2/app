import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import RappelService from './rappel.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import { type IPatient } from '@/shared/model/patient.model';
import MedecinService from '@/entities/medecin/medecin.service';
import { type IMedecin } from '@/shared/model/medecin.model';
import { type IRappel, Rappel } from '@/shared/model/rappel.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'RappelUpdate',
  setup() {
    const rappelService = inject('rappelService', () => new RappelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const rappel: Ref<IRappel> = ref(new Rappel());

    const patientService = inject('patientService', () => new PatientService());

    const patients: Ref<IPatient[]> = ref([]);

    const medecinService = inject('medecinService', () => new MedecinService());

    const medecins: Ref<IMedecin[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRappel = async rappelId => {
      try {
        const res = await rappelService().find(rappelId);
        rappel.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.rappelId) {
      retrieveRappel(route.params.rappelId);
    }

    const initRelationships = () => {
      patientService()
        .retrieve()
        .then(res => {
          patients.value = res.data;
        });
      medecinService()
        .retrieve()
        .then(res => {
          medecins.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      frequence: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      echeance: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      tache: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      patient: {},
      medecins: {},
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
      patients,
      medecins,
      v$,
      t$,
    };
  },
  created(): void {
    this.rappel.medecins = [];
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
            this.alertService.showInfo(this.t$('g2EcomApp.rappel.updated', { param: param.id }));
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
            this.alertService.showSuccess(this.t$('g2EcomApp.rappel.created', { param: param.id }).toString());
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
