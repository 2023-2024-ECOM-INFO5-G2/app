/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MesureAlbumineDetails from './mesure-albumine-details.vue';
import MesureAlbumineService from './mesure-albumine.service';
import AlertService from '@/shared/alert/alert.service';

type MesureAlbumineDetailsComponentType = InstanceType<typeof MesureAlbumineDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesureAlbumineSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MesureAlbumine Management Detail Component', () => {
    let mesureAlbumineServiceStub: SinonStubbedInstance<MesureAlbumineService>;
    let mountOptions: MountingOptions<MesureAlbumineDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mesureAlbumineServiceStub = sinon.createStubInstance<MesureAlbumineService>(MesureAlbumineService);

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
          mesureAlbumineService: () => mesureAlbumineServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesureAlbumineServiceStub.find.resolves(mesureAlbumineSample);
        route = {
          params: {
            mesureAlbumineId: '' + 123,
          },
        };
        const wrapper = shallowMount(MesureAlbumineDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mesureAlbumine).toMatchObject(mesureAlbumineSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesureAlbumineServiceStub.find.resolves(mesureAlbumineSample);
        const wrapper = shallowMount(MesureAlbumineDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
