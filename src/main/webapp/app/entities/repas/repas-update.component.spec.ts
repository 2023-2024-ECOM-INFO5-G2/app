/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import RepasUpdate from './repas-update.vue';
import RepasService from './repas.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';

type RepasUpdateComponentType = InstanceType<typeof RepasUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const repasSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RepasUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Repas Management Update Component', () => {
    let comp: RepasUpdateComponentType;
    let repasServiceStub: SinonStubbedInstance<RepasService>;

    beforeEach(() => {
      route = {};
      repasServiceStub = sinon.createStubInstance<RepasService>(RepasService);
      repasServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          repasService: () => repasServiceStub,
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

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(RepasUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(RepasUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.repas = repasSample;
        repasServiceStub.update.resolves(repasSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(repasServiceStub.update.calledWith(repasSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        repasServiceStub.create.resolves(entity);
        const wrapper = shallowMount(RepasUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.repas = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(repasServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        repasServiceStub.find.resolves(repasSample);
        repasServiceStub.retrieve.resolves([repasSample]);

        // WHEN
        route = {
          params: {
            repasId: '' + repasSample.id,
          },
        };
        const wrapper = shallowMount(RepasUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.repas).toMatchObject(repasSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        repasServiceStub.find.resolves(repasSample);
        const wrapper = shallowMount(RepasUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
