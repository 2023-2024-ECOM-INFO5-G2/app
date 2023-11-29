/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MedecinUpdate from './medecin-update.vue';
import MedecinService from './medecin.service';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import AlerteService from '@/entities/alerte/alerte.service';

type MedecinUpdateComponentType = InstanceType<typeof MedecinUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const medecinSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MedecinUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Medecin Management Update Component', () => {
    let comp: MedecinUpdateComponentType;
    let medecinServiceStub: SinonStubbedInstance<MedecinService>;

    beforeEach(() => {
      route = {};
      medecinServiceStub = sinon.createStubInstance<MedecinService>(MedecinService);
      medecinServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          medecinService: () => medecinServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          alerteService: () =>
            sinon.createStubInstance<AlerteService>(AlerteService, {
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
        const wrapper = shallowMount(MedecinUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.medecin = medecinSample;
        medecinServiceStub.update.resolves(medecinSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medecinServiceStub.update.calledWith(medecinSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        medecinServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MedecinUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.medecin = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medecinServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        medecinServiceStub.find.resolves(medecinSample);
        medecinServiceStub.retrieve.resolves([medecinSample]);

        // WHEN
        route = {
          params: {
            medecinId: '' + medecinSample.id,
          },
        };
        const wrapper = shallowMount(MedecinUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.medecin).toMatchObject(medecinSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        medecinServiceStub.find.resolves(medecinSample);
        const wrapper = shallowMount(MedecinUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
