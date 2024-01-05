/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PatientDetails from './patient-details.vue';
import PatientService from './patient.service';
import AlertService from '@/shared/alert/alert.service';

type PatientDetailsComponentType = InstanceType<typeof PatientDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const patientSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Patient Management Detail Component', () => {
    let patientServiceStub: SinonStubbedInstance<PatientService>;
    let mountOptions: MountingOptions<PatientDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      patientServiceStub = sinon.createStubInstance<PatientService>(PatientService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          patientService: () => patientServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        patientServiceStub.find.resolves(patientSample);
        route = {
          params: {
            patientId: '' + 123,
          },
        };
        const wrapper = shallowMount(PatientDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.patient).toMatchObject(patientSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        patientServiceStub.find.resolves(patientSample);
        const wrapper = shallowMount(PatientDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
