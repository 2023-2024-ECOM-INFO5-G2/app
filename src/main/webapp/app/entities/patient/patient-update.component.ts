import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PatientService from './patient.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import EtablissementService from '@/entities/etablissement/etablissement.service';
import { type IEtablissement } from '@/shared/model/etablissement.model';
import { type IPatient, Patient } from '@/shared/model/patient.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PatientUpdate',
  setup() {
    const patientService = inject('patientService', () => new PatientService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const patient: Ref<IPatient> = ref(new Patient());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);

    const etablissementService = inject('etablissementService', () => new EtablissementService());

    const etablissements: Ref<IEtablissement[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePatient = async patientId => {
      try {
        const res = await patientService().find(patientId);
        res.dateArrivee = new Date(res.dateArrivee);
        patient.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.patientId) {
      retrievePatient(route.params.patientId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
      etablissementService()
        .retrieve()
        .then(res => {
          etablissements.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      prenom: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 64 }).toString(), 64),
      },
      nom: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 64 }).toString(), 64),
      },
      sexe: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 64 }).toString(), 64),
      },
      taille: {
        required: validations.required(t$('entity.validation.required').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 45 }).toString(), 45),
        max: validations.maxValue(t$('entity.validation.max', { max: 290 }).toString(), 290),
      },
      dateDeNaissance: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      numChambre: {
        required: validations.required(t$('entity.validation.required').toString()),
        numeric: validations.numeric(t$('entity.validation.number').toString()),
      },
      dateArrivee: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      infoComplementaires: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 1024 }).toString(), 1024),
      },
      alertes: {},
      rappels: {},
      mesurePoids: {},
      mesureEPAS: {},
      mesureAlbumines: {},
      repas: {},
      users: {},
      etablissement: {},
    };
    const v$ = useVuelidate(validationRules, patient as any);
    v$.value.$validate();

    return {
      patientService,
      alertService,
      patient,
      previousState,
      isSaving,
      currentLanguage,
      users,
      etablissements,
      v$,
      ...useDateFormat({ entityRef: patient }),
      t$,
    };
  },
  created(): void {
    this.patient.users = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.patient.id) {
        this.patientService()
          .update(this.patient)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ecom02App.patient.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.patientService()
          .create(this.patient)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ecom02App.patient.created', { param: param.id }).toString());
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
