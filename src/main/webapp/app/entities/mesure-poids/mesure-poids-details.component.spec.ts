/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MesurePoidsDetails from './mesure-poids-details.vue';
import MesurePoidsService from './mesure-poids.service';
import AlertService from '@/shared/alert/alert.service';

type MesurePoidsDetailsComponentType = InstanceType<typeof MesurePoidsDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesurePoidsSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MesurePoids Management Detail Component', () => {
    let mesurePoidsServiceStub: SinonStubbedInstance<MesurePoidsService>;
    let mountOptions: MountingOptions<MesurePoidsDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mesurePoidsServiceStub = sinon.createStubInstance<MesurePoidsService>(MesurePoidsService);

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
          mesurePoidsService: () => mesurePoidsServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesurePoidsServiceStub.find.resolves(mesurePoidsSample);
        route = {
          params: {
            mesurePoidsId: '' + 123,
          },
        };
        const wrapper = shallowMount(MesurePoidsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mesurePoids).toMatchObject(mesurePoidsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesurePoidsServiceStub.find.resolves(mesurePoidsSample);
        const wrapper = shallowMount(MesurePoidsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
