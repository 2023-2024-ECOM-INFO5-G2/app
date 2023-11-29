/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AlerteUpdate from './alerte-update.vue';
import AlerteService from './alerte.service';
import AlertService from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';

type AlerteUpdateComponentType = InstanceType<typeof AlerteUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const alerteSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AlerteUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Alerte Management Update Component', () => {
    let comp: AlerteUpdateComponentType;
    let alerteServiceStub: SinonStubbedInstance<AlerteService>;

    beforeEach(() => {
      route = {};
      alerteServiceStub = sinon.createStubInstance<AlerteService>(AlerteService);
      alerteServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          alerteService: () => alerteServiceStub,
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
        const wrapper = shallowMount(AlerteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alerte = alerteSample;
        alerteServiceStub.update.resolves(alerteSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(alerteServiceStub.update.calledWith(alerteSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        alerteServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AlerteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alerte = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(alerteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        alerteServiceStub.find.resolves(alerteSample);
        alerteServiceStub.retrieve.resolves([alerteSample]);

        // WHEN
        route = {
          params: {
            alerteId: '' + alerteSample.id,
          },
        };
        const wrapper = shallowMount(AlerteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.alerte).toMatchObject(alerteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        alerteServiceStub.find.resolves(alerteSample);
        const wrapper = shallowMount(AlerteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
