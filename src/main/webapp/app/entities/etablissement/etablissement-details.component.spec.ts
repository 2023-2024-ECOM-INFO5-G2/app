/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EtablissementDetails from './etablissement-details.vue';
import EtablissementService from './etablissement.service';
import AlertService from '@/shared/alert/alert.service';

type EtablissementDetailsComponentType = InstanceType<typeof EtablissementDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const etablissementSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Etablissement Management Detail Component', () => {
    let etablissementServiceStub: SinonStubbedInstance<EtablissementService>;
    let mountOptions: MountingOptions<EtablissementDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      etablissementServiceStub = sinon.createStubInstance<EtablissementService>(EtablissementService);

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
          etablissementService: () => etablissementServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        etablissementServiceStub.find.resolves(etablissementSample);
        route = {
          params: {
            etablissementId: '' + 123,
          },
        };
        const wrapper = shallowMount(EtablissementDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.etablissement).toMatchObject(etablissementSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        etablissementServiceStub.find.resolves(etablissementSample);
        const wrapper = shallowMount(EtablissementDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
