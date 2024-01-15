import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { Line } from 'vue-chartjs';
import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  TimeScale,
  Title,
  Tooltip,
} from 'chart.js';
import 'chartjs-adapter-luxon';
import PatientService from './patient.service';
import MesureEPAService from '../mesure-epa/mesure-epa.service';
import MesurePoidsService from '../mesure-poids/mesure-poids.service';
import MesureAlbumineService from '../mesure-albumine/mesure-albumine.service';
import AlerteService from '../alerte/alerte.service';
import RepasService from '../repas/repas.service';
import RappelService from '../rappel/rappel.service';
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

import { useAlertService } from '@/shared/alert/alert.service';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faArrowsUpDown, faCakeCandles, faDoorOpen, faGenderless, faLocationDot } from '@fortawesome/free-solid-svg-icons';
import type { IRappel } from '../../shared/model/rappel.model';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, PointElement, LineElement, TimeScale);

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
    const rappelService = inject('rappelService', () => new RappelService());
    const repasService = inject('repasService', () => new RepasService());
    const alerteService = inject('alerteService', () => new AlerteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const patient: Ref<IPatient> = ref({});
    const poidsPatient: Ref<Array<IMesurePoids>> = ref([]);
    const EPAPatient: Ref<Array<IMesureEPA>> = ref([]);
    const albuPatient: Ref<Array<IMesureAlbumine>> = ref([]);
    const patientIMC: Ref<Number> = ref(0);
    const chartData: Ref<Object> = ref({});
    const chartOptions: Ref<Object> = ref({
      responsive: true,
      scales: {
        x: {
          type: 'time',
          parsing: false,
          ticks: {
            callback: (value: number) => new Date(value).toLocaleString('fr-FR'),
          },
        },
      },
    });
    const chartDataLoaded: Ref<Boolean> = ref(false);
    const newEPAValue: Ref<Number> = ref(0);
    const newWeightValue: Ref<Number> = ref(0);
    const newAlbuValue: Ref<Number> = ref(0);

    const dangerEPA: Ref<string> = ref('default');
    const dangerWeight: Ref<string> = ref('default');
    const dangerIMC: Ref<string> = ref('default');
    const dangerAlbu: Ref<string> = ref('default');

    const toasts: Ref<Array<Object>> = ref([]);

    const patientMeals: Ref<Array<Object>> = ref([]);
    const tableCurrentPage: Ref<Number> = ref(0);
    const itemsPerPageTable: Ref<Number> = ref(5);
    const mealName: Ref<String> = ref('');
    const mealDesc: Ref<String> = ref('');
    const mealCal: Ref<Number> = ref(0);
    const { t: t$ } = useI18n();

    const showWeightModal: Ref<boolean> = ref(false);
    const showEPAModal: Ref<boolean> = ref(false);
    const showAlbuModal: Ref<boolean> = ref(false);

    const instructionDesc: Ref<string> = ref('');
    const instructionEcheance: Ref<string> = ref('');
    const instructionInterv: Ref<number> = ref(null);
    const instructionOptions: Ref<Array<Object>> = ref([
      {
        value: null,
        text: 'Selectionner un patient',
      },
    ]);

    const patientAlerts: Ref<Array<Object>> = ref([]);

    const retrievePatient = async (patientId: string | string[]) => {
      try {
        const res = await patientService().find(patientId);
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
          refreshData();
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
          refreshData();
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
          refreshData();
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

    const updatePoidsValues = async () => {
      try {
        for (const measure of poidsPatient.value) {
          measure.date = measure.date.concat('Z');
          await mesurePoidsService().update(measure);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
      await retrievePatientMesures(patient.value.id);
      refreshData();
      showWeightModal.value = false;
    };

    const removePoidsValue = async (index: number) => {
      const removed: IMesurePoids = poidsPatient.value.splice(index, 1)[0];
      await mesurePoidsService().delete(removed.id);
      refreshData();
    };

    const updateEPAValues = async () => {
      try {
        for (const measure of EPAPatient.value) {
          measure.date = measure.date.concat('Z');
          await mesureEPAService().update(measure);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
      await retrievePatientMesures(patient.value.id);
      refreshData();
      showEPAModal.value = false;
    };

    const removeEPAValue = async (index: number) => {
      const removed: IMesureEPA = EPAPatient.value.splice(index, 1)[0];
      await mesureEPAService().delete(removed.id);
      refreshData();
    };

    const updateAlbuValues = async () => {
      try {
        for (const measure of albuPatient.value) {
          measure.date = measure.date.concat('Z');
          await mesureAlbumineService().update(measure);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
      await retrievePatientMesures(patient.value.id);
      refreshData();
      showAlbuModal.value = false;
    };

    const removeAlbuValue = async (index: number) => {
      const removed: IMesureAlbumine = albuPatient.value.splice(index, 1)[0];
      await mesureAlbumineService().delete(removed.id);
      refreshData();
    };

    const calculIMC = (patientHeight: string, patientWeight: string) => {
      return Math.round(Number(patientWeight) / (((Number(patientHeight) / 100) * Number(patientHeight)) / 100));
    };

    const updateDanger = () => {
      dangerWeight.value = 'default';
      dangerIMC.value = 'default';
      dangerAlbu.value = 'default';
      dangerWeight.value = 'default';
      for (const alert of patientAlerts.value) {
        console.log(alert);
        switch (alert.code) {
          case 10:
          case 11:
          case 12:
          case 13:
          case 14:
          case 15:
            dangerWeight.value = 'warning';
            break;
          case 20:
            dangerIMC.value = 'warning';
            break;
          case 21:
            dangerIMC.value = 'danger';
            break;
          case 30:
            dangerAlbu.value = 'warning';
            break;
          case 31:
            dangerAlbu.value = 'danger';
            break;
          case 40:
            dangerEPA.value = 'warning';
            break;
        }
      }
    };

    const sortArrays = () => {
      poidsPatient.value.sort((a: IMesurePoids, b: IMesurePoids) => +new Date(b.date) - +new Date(a.date));
      EPAPatient.value.sort((a: IMesureEPA, b: IMesureEPA) => +new Date(b.date) - +new Date(a.date));
      albuPatient.value.sort((a: IMesureAlbumine, b: IMesureAlbumine) => +new Date(b.date) - +new Date(a.date));
    };

    const refreshData = () => {
      checkForAlert(patient.value.id).then(() => {
        patientIMC.value = calculIMC(patient.value.taille, poidsPatient.value[0]?.valeur);
        sortArrays();
        refreshCharts();
        updateDanger();
      });
    };

    const refreshCharts = () => {
      const weightValues = [];
      const EPAValues = [];
      for (const weightEntry of poidsPatient.value) {
        weightValues.push({
          x: Date.parse(weightEntry.date),
          y: weightEntry.valeur,
        });
      }
      for (const EPAEntry of EPAPatient.value) {
        EPAValues.push({
          x: Date.parse(EPAEntry.date),
          y: EPAEntry.valeur,
        });
      }

      weightValues.sort((a: any, b: any) => +new Date(a.x) - +new Date(b.x));
      EPAValues.sort((a: any, b: any) => +new Date(a.x) - +new Date(b.x));

      chartData.value = {
        datasets: [
          {
            label: 'poids (kg)',
            data: weightValues,
            borderColor: 'rgb(255, 125, 100)',
          },
          {
            label: 'EPA',
            data: EPAValues,
            borderColor: 'rgb(45, 80, 255)',
          },
        ],
      };
      chartDataLoaded.value = true;
    };

    const retrievePatientMesures = async (patientId: string | string[]) => {
      try {
        const resAlbu = await mesureAlbumineService().retrieve();
        const resPoids = await mesurePoidsService().retrieve();
        const resEPA = await mesureEPAService().retrieve();

        const weightValues = resPoids.data.filter((o: IMesurePoids) => o.patient !== null && o.patient.id === Number(patientId));
        const albuValues = resAlbu.data.filter((o: IMesureAlbumine) => o.patient !== null && o.patient.id === Number(patientId));
        const EPAValues = resEPA.data.filter((o: IMesureEPA) => o.patient !== null && o.patient.id === Number(patientId));

        weightValues.map((m: IMesurePoids) => (m.date = m.date.substring(0, 19)));
        albuValues.map((m: IMesureAlbumine) => (m.date = m.date.substring(0, 19)));
        EPAValues.map((m: IMesureEPA) => (m.date = m.date.substring(0, 19)));

        poidsPatient.value = weightValues;
        albuPatient.value = albuValues;
        EPAPatient.value = EPAValues;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const retrievePatientMeals = async (patientId: string | string[]) => {
      try {
        const res = await repasService().retrieve();
        patientMeals.value = [];
        for (const meal of res.data) {
          if (meal.patients?.filter((patient: IPatient) => patient.id === Number(patientId)).length === 0) continue;
          delete meal.patients;
          patientMeals.value.push(meal);
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const checkForAlert = async (patientId: string | string[]) => {
      try {
        const res = await alerteService().retrieve();
        patientAlerts.value = [];
        for (const alert of res.data) {
          if (alert.patient?.id === Number(patientId)) {
            alertService.showRed('Alerte', alert.description);
            patientAlerts.value.push(alert);
          }
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.patientId) {
      retrievePatient(route.params.patientId).then(() =>
        retrievePatientMesures(route.params.patientId).then(() => {
          refreshData();
        }),
      );
      retrievePatientMeals(route.params.patientId);
    }

    const addInstruction = async () => {
      const rappel: IRappel = {
        date: new Date().toISOString(),
        echeance: new Date(instructionEcheance.value).toISOString(),
        intervaleJours: instructionInterv.value,
        tache: instructionDesc.value,
        feeDansLetang: true,
        patient: { id: patient.value.id },
      };
      try {
        await rappelService().create(rappel);
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      ...dateFormat,
      alertService,
      patient,
      albuPatient,
      poidsPatient,
      EPAPatient,
      patientIMC,
      chartData,
      chartOptions,
      chartDataLoaded,
      newEPAValue,
      newWeightValue,
      newAlbuValue,
      dangerEPA,
      dangerWeight,
      dangerIMC,
      dangerAlbu,
      toasts,
      patientAlerts,

      patientMeals,
      tableCurrentPage,
      itemsPerPageTable,
      mealName,
      mealDesc,
      mealCal,
      ...dataUtils,

      showWeightModal,
      showEPAModal,
      showAlbuModal,

      instructionDesc,
      instructionOptions,
      instructionEcheance,
      instructionInterv,

      previousState,
      t$: useI18n().t,
      addEPAValue,
      addPoidsValue,
      addAlbuValue,
      addMeal,
      updatePoidsValues,
      removePoidsValue,
      updateEPAValues,
      removeEPAValue,
      updateAlbuValues,
      removeAlbuValue,
      addInstruction,
    };
  },
});
