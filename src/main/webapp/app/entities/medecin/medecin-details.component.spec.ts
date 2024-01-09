/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MedecinDetails from './medecin-details.vue';
import MedecinService from './medecin.service';
import AlertService from '@/shared/alert/alert.service';

type MedecinDetailsComponentType = InstanceType<typeof MedecinDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const medecinSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Medecin Management Detail Component', () => {
    let medecinServiceStub: SinonStubbedInstance<MedecinService>;
    let mountOptions: MountingOptions<MedecinDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      medecinServiceStub = sinon.createStubInstance<MedecinService>(MedecinService);

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
          medecinService: () => medecinServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        medecinServiceStub.find.resolves(medecinSample);
        route = {
          params: {
            medecinId: '' + 123,
          },
        };
        const wrapper = shallowMount(MedecinDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.medecin).toMatchObject(medecinSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        medecinServiceStub.find.resolves(medecinSample);
        const wrapper = shallowMount(MedecinDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
