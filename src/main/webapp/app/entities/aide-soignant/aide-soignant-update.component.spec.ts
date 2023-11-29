/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AideSoignantUpdate from './aide-soignant-update.vue';
import AideSoignantService from './aide-soignant.service';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import PatientService from '@/entities/patient/patient.service';

type AideSoignantUpdateComponentType = InstanceType<typeof AideSoignantUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const aideSoignantSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AideSoignantUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AideSoignant Management Update Component', () => {
    let comp: AideSoignantUpdateComponentType;
    let aideSoignantServiceStub: SinonStubbedInstance<AideSoignantService>;

    beforeEach(() => {
      route = {};
      aideSoignantServiceStub = sinon.createStubInstance<AideSoignantService>(AideSoignantService);
      aideSoignantServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          aideSoignantService: () => aideSoignantServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          patientService: () =>
            sinon.createStubInstance<PatientService>(PatientService, {
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
        const wrapper = shallowMount(AideSoignantUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.aideSoignant = aideSoignantSample;
        aideSoignantServiceStub.update.resolves(aideSoignantSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(aideSoignantServiceStub.update.calledWith(aideSoignantSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        aideSoignantServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AideSoignantUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.aideSoignant = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(aideSoignantServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        aideSoignantServiceStub.find.resolves(aideSoignantSample);
        aideSoignantServiceStub.retrieve.resolves([aideSoignantSample]);

        // WHEN
        route = {
          params: {
            aideSoignantId: '' + aideSoignantSample.id,
          },
        };
        const wrapper = shallowMount(AideSoignantUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.aideSoignant).toMatchObject(aideSoignantSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        aideSoignantServiceStub.find.resolves(aideSoignantSample);
        const wrapper = shallowMount(AideSoignantUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
