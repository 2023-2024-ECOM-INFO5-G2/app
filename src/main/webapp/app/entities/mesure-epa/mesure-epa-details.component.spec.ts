/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MesureEPADetails from './mesure-epa-details.vue';
import MesureEPAService from './mesure-epa.service';
import AlertService from '@/shared/alert/alert.service';

type MesureEPADetailsComponentType = InstanceType<typeof MesureEPADetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesureEPASample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MesureEPA Management Detail Component', () => {
    let mesureEPAServiceStub: SinonStubbedInstance<MesureEPAService>;
    let mountOptions: MountingOptions<MesureEPADetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mesureEPAServiceStub = sinon.createStubInstance<MesureEPAService>(MesureEPAService);

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
          mesureEPAService: () => mesureEPAServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesureEPAServiceStub.find.resolves(mesureEPASample);
        route = {
          params: {
            mesureEPAId: '' + 123,
          },
        };
        const wrapper = shallowMount(MesureEPADetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mesureEPA).toMatchObject(mesureEPASample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesureEPAServiceStub.find.resolves(mesureEPASample);
        const wrapper = shallowMount(MesureEPADetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
