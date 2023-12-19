/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EtablissementUpdate from './etablissement-update.vue';
import EtablissementService from './etablissement.service';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

type EtablissementUpdateComponentType = InstanceType<typeof EtablissementUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const etablissementSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EtablissementUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Etablissement Management Update Component', () => {
    let comp: EtablissementUpdateComponentType;
    let etablissementServiceStub: SinonStubbedInstance<EtablissementService>;

    beforeEach(() => {
      route = {};
      etablissementServiceStub = sinon.createStubInstance<EtablissementService>(EtablissementService);
      etablissementServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          etablissementService: () => etablissementServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EtablissementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.etablissement = etablissementSample;
        etablissementServiceStub.update.resolves(etablissementSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(etablissementServiceStub.update.calledWith(etablissementSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        etablissementServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EtablissementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.etablissement = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(etablissementServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        etablissementServiceStub.find.resolves(etablissementSample);
        etablissementServiceStub.retrieve.resolves([etablissementSample]);

        // WHEN
        route = {
          params: {
            etablissementId: '' + etablissementSample.id,
          },
        };
        const wrapper = shallowMount(EtablissementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.etablissement).toMatchObject(etablissementSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        etablissementServiceStub.find.resolves(etablissementSample);
        const wrapper = shallowMount(EtablissementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
