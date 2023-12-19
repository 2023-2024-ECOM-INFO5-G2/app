/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import MesurePoidsUpdate from './mesure-poids-update.vue';
import MesurePoidsService from './mesure-poids.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';

type MesurePoidsUpdateComponentType = InstanceType<typeof MesurePoidsUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesurePoidsSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MesurePoidsUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MesurePoids Management Update Component', () => {
    let comp: MesurePoidsUpdateComponentType;
    let mesurePoidsServiceStub: SinonStubbedInstance<MesurePoidsService>;

    beforeEach(() => {
      route = {};
      mesurePoidsServiceStub = sinon.createStubInstance<MesurePoidsService>(MesurePoidsService);
      mesurePoidsServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          mesurePoidsService: () => mesurePoidsServiceStub,
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
        const wrapper = shallowMount(MesurePoidsUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(MesurePoidsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mesurePoids = mesurePoidsSample;
        mesurePoidsServiceStub.update.resolves(mesurePoidsSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mesurePoidsServiceStub.update.calledWith(mesurePoidsSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mesurePoidsServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MesurePoidsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mesurePoids = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mesurePoidsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mesurePoidsServiceStub.find.resolves(mesurePoidsSample);
        mesurePoidsServiceStub.retrieve.resolves([mesurePoidsSample]);

        // WHEN
        route = {
          params: {
            mesurePoidsId: '' + mesurePoidsSample.id,
          },
        };
        const wrapper = shallowMount(MesurePoidsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mesurePoids).toMatchObject(mesurePoidsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesurePoidsServiceStub.find.resolves(mesurePoidsSample);
        const wrapper = shallowMount(MesurePoidsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
