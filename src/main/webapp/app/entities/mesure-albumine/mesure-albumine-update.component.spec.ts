/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import MesureAlbumineUpdate from './mesure-albumine-update.vue';
import MesureAlbumineService from './mesure-albumine.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';

type MesureAlbumineUpdateComponentType = InstanceType<typeof MesureAlbumineUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mesureAlbumineSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MesureAlbumineUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MesureAlbumine Management Update Component', () => {
    let comp: MesureAlbumineUpdateComponentType;
    let mesureAlbumineServiceStub: SinonStubbedInstance<MesureAlbumineService>;

    beforeEach(() => {
      route = {};
      mesureAlbumineServiceStub = sinon.createStubInstance<MesureAlbumineService>(MesureAlbumineService);
      mesureAlbumineServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          mesureAlbumineService: () => mesureAlbumineServiceStub,
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
        const wrapper = shallowMount(MesureAlbumineUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(MesureAlbumineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mesureAlbumine = mesureAlbumineSample;
        mesureAlbumineServiceStub.update.resolves(mesureAlbumineSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mesureAlbumineServiceStub.update.calledWith(mesureAlbumineSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mesureAlbumineServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MesureAlbumineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mesureAlbumine = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mesureAlbumineServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mesureAlbumineServiceStub.find.resolves(mesureAlbumineSample);
        mesureAlbumineServiceStub.retrieve.resolves([mesureAlbumineSample]);

        // WHEN
        route = {
          params: {
            mesureAlbumineId: '' + mesureAlbumineSample.id,
          },
        };
        const wrapper = shallowMount(MesureAlbumineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mesureAlbumine).toMatchObject(mesureAlbumineSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mesureAlbumineServiceStub.find.resolves(mesureAlbumineSample);
        const wrapper = shallowMount(MesureAlbumineUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
