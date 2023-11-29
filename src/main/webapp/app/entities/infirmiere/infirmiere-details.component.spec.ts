/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InfirmiereDetails from './infirmiere-details.vue';
import InfirmiereService from './infirmiere.service';
import AlertService from '@/shared/alert/alert.service';

type InfirmiereDetailsComponentType = InstanceType<typeof InfirmiereDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const infirmiereSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Infirmiere Management Detail Component', () => {
    let infirmiereServiceStub: SinonStubbedInstance<InfirmiereService>;
    let mountOptions: MountingOptions<InfirmiereDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      infirmiereServiceStub = sinon.createStubInstance<InfirmiereService>(InfirmiereService);

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
          infirmiereService: () => infirmiereServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        infirmiereServiceStub.find.resolves(infirmiereSample);
        route = {
          params: {
            infirmiereId: '' + 123,
          },
        };
        const wrapper = shallowMount(InfirmiereDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.infirmiere).toMatchObject(infirmiereSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        infirmiereServiceStub.find.resolves(infirmiereSample);
        const wrapper = shallowMount(InfirmiereDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
