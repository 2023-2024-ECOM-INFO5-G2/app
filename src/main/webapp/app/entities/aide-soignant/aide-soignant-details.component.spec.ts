/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AideSoignantDetails from './aide-soignant-details.vue';
import AideSoignantService from './aide-soignant.service';
import AlertService from '@/shared/alert/alert.service';

type AideSoignantDetailsComponentType = InstanceType<typeof AideSoignantDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const aideSoignantSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AideSoignant Management Detail Component', () => {
    let aideSoignantServiceStub: SinonStubbedInstance<AideSoignantService>;
    let mountOptions: MountingOptions<AideSoignantDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      aideSoignantServiceStub = sinon.createStubInstance<AideSoignantService>(AideSoignantService);

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
          aideSoignantService: () => aideSoignantServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        aideSoignantServiceStub.find.resolves(aideSoignantSample);
        route = {
          params: {
            aideSoignantId: '' + 123,
          },
        };
        const wrapper = shallowMount(AideSoignantDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.aideSoignant).toMatchObject(aideSoignantSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        aideSoignantServiceStub.find.resolves(aideSoignantSample);
        const wrapper = shallowMount(AideSoignantDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
