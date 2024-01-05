/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import RappelDetails from './rappel-details.vue';
import RappelService from './rappel.service';
import AlertService from '@/shared/alert/alert.service';

type RappelDetailsComponentType = InstanceType<typeof RappelDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const rappelSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Rappel Management Detail Component', () => {
    let rappelServiceStub: SinonStubbedInstance<RappelService>;
    let mountOptions: MountingOptions<RappelDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      rappelServiceStub = sinon.createStubInstance<RappelService>(RappelService);

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
          rappelService: () => rappelServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        rappelServiceStub.find.resolves(rappelSample);
        route = {
          params: {
            rappelId: '' + 123,
          },
        };
        const wrapper = shallowMount(RappelDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.rappel).toMatchObject(rappelSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        rappelServiceStub.find.resolves(rappelSample);
        const wrapper = shallowMount(RappelDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
