import { defineComponent, inject, ref, provide, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { Line } from 'vue-chartjs';
import { BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, LineElement, PointElement, Title, Tooltip } from 'chart.js';
import PatientService from './patient.service';
import MesureEPAService from '../mesure-epa/mesure-epa.service';
import MesurePoidsService from '../mesure-poids/mesure-poids.service';
import MesureAlbumineService from '../mesure-albumine/mesure-albumine.service';
import { useDateFormat } from '@/shared/composables';
// @ts-ignore
import useDataUtils from '@/shared/data/data-utils.service';
// @ts-ignore
import { type IPatient } from '@/shared/model/patient.model';
// @ts-ignore
import { type IMesureEPA } from '@/shared/model/mesure-epa.model';
// @ts-ignore
import { type IMesurePoids } from '@/shared/model/mesure-poids.model';
// @ts-ignore
import { type IMesureAlbumine } from '@/shared/model/mesure-albumine.model';
// @ts-ignore
import { type IRepas } from '@/shared/model/repas.model';
// @ts-ignore
import { type IRepas } from '@/shared/model/repas.model';
// @ts-ignore
import { useAlertService } from '@/shared/alert/alert.service';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faArrowsUpDown, faCakeCandles, faDoorOpen, faGenderless, faLocationDot } from '@fortawesome/free-solid-svg-icons';
import { useValidation } from '../../shared/composables';
import { useVuelidate } from '@vuelidate/core';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, PointElement, LineElement);

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PatientDetails',
  components: { Line },
  setup() {
    library.add(faLocationDot);
    library.add(faCakeCandles);
    library.add(faGenderless);
    library.add(faArrowsUpDown);
    library.add(faDoorOpen);
    const dateFormat = useDateFormat();
    const patientService = inject('patientService', () => new PatientService());
    const mesureEPAService = inject('mesureEPAService', () => new MesureEPAService());
    const mesurePoidsService = inject('mesurePoidsService', () => new MesurePoidsService());
    const mesureAlbumineService = inject('mesureAlbumineService', () => new MesureAlbumineService());
    const repasService = inject('repasService', () => new RepasService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const patient: Ref<IPatient> = ref({});
    const poidsPatient: Ref<Array<IMesure>> = ref([]);
    const EPAPatient: Ref<Array<IMesure>> = ref([]);
    const albuPatient: Ref<Array<IMesure>> = ref([]);
    const patientIMC: Ref<Number> = ref(0);
    const weightChartData: Ref<Object> = ref({});
    const EPAChartData: Ref<Object> = ref({});
    const chartOptions: Ref<Object> = ref({
      responsive: true,
    });
    const weightChartLoaded: Ref<Boolean> = ref(false);
    const EPAChartLoaded: Ref<Boolean> = ref(false);
    const newEPAValue: Ref<Number> = ref(0);
    const newWeightValue: Ref<Number> = ref(0);
    const newAlbuValue: Ref<Number> = ref(0);

    const dangerEPA: Ref<Boolean> = ref(false);
    const dangerWeight: Ref<Boolean> = ref(false);

    const toasts: Ref<Array<Object>> = ref([]);

    const patientMeals: Ref<Array<Object>> = ref([]);
    const tableCurrentPage: Ref<Number> = ref(0);
    const itemsPerPageTable: Ref<Number> = ref(5);
    const mealName: Ref<String> = ref('');
    const mealDesc: Ref<String> = ref('');
    const mealCal: Ref<Number> = ref(0);
    const { t: t$ } = useI18n();

    const retrievePatient = async (patientId: string | string[]) => {
      try {
        const res = await patientService().find(Number(patientId));
        patient.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const addAlbuValue = async () => {
      // TODO: add validation using vuelidate

      try {
        if (newAlbuValue.value === null) alertService.showError('Donnée incorrecte');
        else if (Number(newAlbuValue.value) <= 0 || Number(newAlbuValue.value) >= 50) alertService.showError('Albumine (0 - 50)');
        else {
          // Create a new Albu entry object
          const newAlbuEntry = {
            date: new Date().toISOString(),
            valeur: Number(newAlbuValue.value),
            nomValeur: 'albumine',
            patient: patient.value,
          };

          // Add the new Albu entry to the albuPatient array
          albuPatient.value.push(newAlbuEntry);

          // Save the new Albu entry to the server
          await mesureAlbumineService().create(newAlbuEntry);
          await retrievePatientMesures(patient.value.id);
          patientIMC.value = calculIMC(patient.value.taille, poidsPatient.value[poidsPatient.value.length - 1]?.valeur);
          refreshCharts();
          updateDanger();
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const addPoidsValue = async () => {
      try {
        if (newWeightValue.value === null) alertService.showError('Donnée incorrecte');
        else if (Number(newWeightValue.value) <= 0 || Number(newWeightValue.value) >= 500) alertService.showError('Poids (0 - 500)');
        else {
          // Create a new Poids entry object
          const newPoidsEntry = {
            date: new Date().toISOString(),
            valeur: Number(newWeightValue.value),
            patient: patient.value,
          };

          // Add the new Poids entry to the poidsPatient array
          poidsPatient.value.push(newPoidsEntry);

          // Save the new Poids entry to the server
          await mesurePoidsService().create(newPoidsEntry);
          await retrievePatientMesures(patient.value.id);
          patientIMC.value = calculIMC(patient.value.taille, poidsPatient.value[poidsPatient.value.length - 1]?.valeur);
          refreshCharts();
          updateDanger();
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const addEPAValue = async () => {
      try {
        if (newEPAValue.value === null) alertService.showError('Donnée incorrecte');
        else if (Number(newEPAValue.value) <= 0 || Number(newEPAValue.value) >= 10) alertService.showError('EPA (0 - 10)');
        else {
          // Create a new EPA entry object
          const newEPAEntry = {
            date: new Date().toISOString(),
            valeur: Number(newEPAValue.value),
            patient: patient.value,
          };

          // Add the new EPA entry to the EPAPatient array
          EPAPatient.value.push(newEPAEntry);

          // Save the new EPA entry to the server
          await mesureEPAService().create(newEPAEntry);
          await retrievePatientMesures(patient.value.id);
          patientIMC.value = calculIMC(patient.value.taille, poidsPatient.value[poidsPatient.value.length - 1]?.valeur);
          refreshCharts();
          updateDanger();
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const addMeal = async () => {
      try {
        if (Number(mealCal.value) <= 0) alertService.showError('Donnée incorrecte, calories doit être positif');
        else {
          // Create a new EPA entry object
          const newMeal = {
            nom: mealName.value,
            description: mealDesc.value,
            apportCalorique: mealCal.value,
            patients: [patient.value],
          };

          await repasService().create(newMeal);
          await retrievePatientMeals(patient.value.id);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const addMeal = async () => {
      try {
        if (Number(mealCal.value) <= 0) alertService.showError('Donnée incorrecte, calories doit être positif');
        else {
          // Create a new EPA entry object
          const newMeal = {
            nom: mealName.value,
            description: mealDesc.value,
            apportCalorique: mealCal.value,
            patients: [patient.value],
          };

          await repasService().create(newMeal);
          await retrievePatientMeals(patient.value.id);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const calculIMC = (patientHeight: string, patientWeight: string) => {
      return Math.round(Number(patientWeight) / (((Number(patientHeight) / 100) * Number(patientHeight)) / 100));
    };

    const updateDanger = () => {
      dangerEPA.value = EPAPatient.value[EPAPatient.value.length - 1]?.valeur < 7;
      if (+new Date() - +new Date(patient.value.dateArrivee) >= 2 && poidsPatient.value?.length === 0) {
        dangerWeight.value = true;
      }
    };

    const refreshCharts = () => {
      const weightValues = [];
      const EPAValues = [];
      for (const weightEntry of poidsPatient.value) {
        weightValues.push({
          x: weightEntry.date,
          y: weightEntry.valeur,
        });
      }
      for (const EPAEntry of EPAPatient.value) {
        EPAValues.push({
          x: EPAEntry.date,
          y: EPAEntry.valeur,
        });
      }
      weightChartData.value = {
        datasets: [
          {
            label: 'poids (kg)',
            data: weightValues,
            borderColor: 'rgb(255, 125, 100)',
          },
        ],
      };
      EPAChartData.value = {
        datasets: [
          {
            label: 'EPA',
            data: EPAValues,
            borderColor: 'rgb(45, 80, 255)',
          },
        ],
      };
      weightChartLoaded.value = true;
      EPAChartLoaded.value = true;
    };

    const retrievePatientMesures = async (patientId: string | string[]) => {
      try {
        const resAlbu = await mesureAlbumineService().retrieve();
        const resPoids = await mesurePoidsService().retrieve();
        const resEPA = await mesureEPAService().retrieve();

        poidsPatient.value = resAlbu.data.filter((o: IMesurePoids) => o.patient !== null && o.patient.id === Number(patientId));
        albuPatient.value = resPoids.data.filter((o: IMesureAlbumine) => o.patient !== null && o.patient.id === Number(patientId));
        EPAPatient.value = resEPA.data.filter((o: IMesureEPA) => o.patient !== null && o.patient.id === Number(patientId));

        poidsPatient.value.sort((a: IMesurePoids, b: IMesurePoids) => +new Date(a.date) - +new Date(b.date));
        EPAPatient.value.sort((a: IMesureEPA, b: IMesureEPA) => +new Date(a.date) - +new Date(b.date));
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const retrievePatientMeals = async (patientId: string | string[]) => {
      try {
        const res = await repasService().retrieve();
        patientMeals.value = [];
        for (const meal of res.data) {
          if (meal.patients.filter((patient: IPatient) => patient.id === Number(patientId)).length === 0) continue;
          delete meal.patients;
          patientMeals.value.push(meal);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const retrievePatientMeals = async (patientId: string | string[]) => {
      try {
        const res = await repasService().retrieve();
        patientMeals.value = [];
        for (const meal of res.data) {
          if (meal.patients.filter((patient: IPatient) => patient.id === Number(patientId)).length === 0) continue;
          delete meal.patients;
          patientMeals.value.push(meal);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.patientId) {
      retrievePatient(route.params.patientId).then(() =>
        retrievePatientMesures(route.params.patientId).then(() => {
          patientIMC.value = calculIMC(patient.value.taille, poidsPatient.value[poidsPatient.value.length - 1]?.valeur);
          refreshCharts();
          updateDanger();
        }),
      );
      retrievePatientMeals(route.params.patientId);
    }

    return {
      ...dateFormat,
      alertService,
      patient,
      albuPatient,
      poidsPatient,
      EPAPatient,
      patientIMC,
      weightChartData,
      EPAChartData,
      chartOptions,
      weightChartLoaded,
      EPAChartLoaded,
      newEPAValue,
      newWeightValue,
      newAlbuValue,
      dangerEPA,
      dangerWeight,
      toasts,

      patientMeals,
      tableCurrentPage,
      itemsPerPageTable,
      mealName,
      mealDesc,
      mealCal,
      ...dataUtils,

      previousState,
      t$: useI18n().t,
      addEPAValue,
      addPoidsValue,
      addAlbuValue,
      addMeal,
    };
  },
});
