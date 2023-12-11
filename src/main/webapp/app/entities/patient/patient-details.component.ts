import { defineComponent, inject, ref, provide, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { Line } from 'vue-chartjs';
import { BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, LineElement, PointElement, Title, Tooltip } from 'chart.js';
import PatientService from './patient.service';
import MesureService from '../mesure/mesure.service';
import RepasService from '../repas/repas.service';
// @ts-ignore
import useDataUtils from '@/shared/data/data-utils.service';
// @ts-ignore
import { type IPatient } from '@/shared/model/patient.model';
// @ts-ignore
import { type IMesure } from '@/shared/model/mesure.model';
// @ts-ignore
import { type IRepas } from '@/shared/model/repas.model';
// @ts-ignore
import { useAlertService } from '@/shared/alert/alert.service';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faArrowsUpDown, faCakeCandles, faDoorOpen, faGenderless, faLocationDot } from '@fortawesome/free-solid-svg-icons';

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

    const patientService = inject('patientService', () => new PatientService());
    const mesureService = inject('mesureService', () => new MesureService());
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

    const retrievePatient = async (patientId: string | string[]) => {
      try {
        const res = await patientService().find(Number(patientId));
        patient.value = res;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const addAlbuValue = async () => {
      try {
        if (Number(newAlbuValue.value) <= 0 || newAlbuValue.value === null) alertService.showError('Donnée incorrecte');
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
          await mesureService().create(newAlbuEntry);
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
        if (Number(newWeightValue.value) <= 0 || newWeightValue.value === null) alertService.showError('Donnée incorrecte');
        else {
          // Create a new Poids entry object
          const newPoidsEntry = {
            date: new Date().toISOString(),
            valeur: Number(newWeightValue.value),
            nomValeur: 'poids',
            patient: patient.value,
          };

          // Add the new Poids entry to the poidsPatient array
          poidsPatient.value.push(newPoidsEntry);

          // Save the new Poids entry to the server
          await mesureService().create(newPoidsEntry);
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
        if (Number(newEPAValue.value) <= 0 || newEPAValue.value === null) alertService.showError('Donnée incorrecte');
        else {
          // Create a new EPA entry object
          const newEPAEntry = {
            date: new Date().toISOString(),
            valeur: Number(newEPAValue.value),
            nomValeur: 'EPA',
            patient: patient.value,
          };

          // Add the new EPA entry to the EPAPatient array
          EPAPatient.value.push(newEPAEntry);

          // Save the new EPA entry to the server
          await mesureService().create(newEPAEntry);
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
        const res = await mesureService().retrieve();
        const patientMesures = res.data.filter((o: IMesure) => o.patient !== null && o.patient.id === Number(patientId));

        poidsPatient.value = patientMesures.filter((o: IMesure) => o.nomValeur === 'poids');
        poidsPatient.value.sort((a: IMesure, b: IMesure) => +new Date(a.date) - +new Date(b.date));
        EPAPatient.value = patientMesures.filter((o: IMesure) => o.nomValeur === 'EPA');
        albuPatient.value = patientMesures.filter((o: IMesure) => o.nomValeur === 'albumine');
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
