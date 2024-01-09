/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AlerteDetails from './alerte-details.vue';
import AlerteService from './alerte.service';
import AlertService from '@/shared/alert/alert.service';

type AlerteDetailsComponentType = InstanceType<typeof AlerteDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const alerteSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Alerte Management Detail Component', () => {
    let alerteServiceStub: SinonStubbedInstance<AlerteService>;
    let mountOptions: MountingOptions<AlerteDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      alerteServiceStub = sinon.createStubInstance<AlerteService>(AlerteService);

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
          alerteService: () => alerteServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        alerteServiceStub.find.resolves(alerteSample);
        route = {
          params: {
            alerteId: '' + 123,
          },
        };
        const wrapper = shallowMount(AlerteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.alerte).toMatchObject(alerteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        alerteServiceStub.find.resolves(alerteSample);
        const wrapper = shallowMount(AlerteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
