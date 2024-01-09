/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MesureUpdate from './mesure-update.vue';
import MesureService from './mesure.service';
import AlertService from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';

type MesureUpdateComponentType = InstanceType<typeof MesureUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesureSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MesureUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Mesure Management Update Component', () => {
    let comp: MesureUpdateComponentType;
    let mesureServiceStub: SinonStubbedInstance<MesureService>;

    beforeEach(() => {
      route = {};
      mesureServiceStub = sinon.createStubInstance<MesureService>(MesureService);
      mesureServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          mesureService: () => mesureServiceStub,
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
        const wrapper = shallowMount(MesureUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mesure = mesureSample;
        mesureServiceStub.update.resolves(mesureSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mesureServiceStub.update.calledWith(mesureSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mesureServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MesureUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mesure = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mesureServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mesureServiceStub.find.resolves(mesureSample);
        mesureServiceStub.retrieve.resolves([mesureSample]);

        // WHEN
        route = {
          params: {
            mesureId: '' + mesureSample.id,
          },
        };
        const wrapper = shallowMount(MesureUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mesure).toMatchObject(mesureSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesureServiceStub.find.resolves(mesureSample);
        const wrapper = shallowMount(MesureUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
