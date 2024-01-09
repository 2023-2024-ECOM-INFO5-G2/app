/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import RepasDetails from './repas-details.vue';
import RepasService from './repas.service';
import AlertService from '@/shared/alert/alert.service';

type RepasDetailsComponentType = InstanceType<typeof RepasDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const repasSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Repas Management Detail Component', () => {
    let repasServiceStub: SinonStubbedInstance<RepasService>;
    let mountOptions: MountingOptions<RepasDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      repasServiceStub = sinon.createStubInstance<RepasService>(RepasService);

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
          repasService: () => repasServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        repasServiceStub.find.resolves(repasSample);
        route = {
          params: {
            repasId: '' + 123,
          },
        };
        const wrapper = shallowMount(RepasDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.repas).toMatchObject(repasSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        repasServiceStub.find.resolves(repasSample);
        const wrapper = shallowMount(RepasDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
