/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MesureDetails from './mesure-details.vue';
import MesureService from './mesure.service';
import AlertService from '@/shared/alert/alert.service';

type MesureDetailsComponentType = InstanceType<typeof MesureDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesureSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Mesure Management Detail Component', () => {
    let mesureServiceStub: SinonStubbedInstance<MesureService>;
    let mountOptions: MountingOptions<MesureDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mesureServiceStub = sinon.createStubInstance<MesureService>(MesureService);

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
          mesureService: () => mesureServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesureServiceStub.find.resolves(mesureSample);
        route = {
          params: {
            mesureId: '' + 123,
          },
        };
        const wrapper = shallowMount(MesureDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mesure).toMatchObject(mesureSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesureServiceStub.find.resolves(mesureSample);
        const wrapper = shallowMount(MesureDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
