/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import PatientUpdate from './patient-update.vue';
import PatientService from './patient.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import EtablissementService from '@/entities/etablissement/etablissement.service';

type PatientUpdateComponentType = InstanceType<typeof PatientUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const patientSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PatientUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Patient Management Update Component', () => {
    let comp: PatientUpdateComponentType;
    let patientServiceStub: SinonStubbedInstance<PatientService>;

    beforeEach(() => {
      route = {};
      patientServiceStub = sinon.createStubInstance<PatientService>(PatientService);
      patientServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          patientService: () => patientServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          etablissementService: () =>
            sinon.createStubInstance<EtablissementService>(EtablissementService, {
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
        const wrapper = shallowMount(PatientUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(PatientUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.patient = patientSample;
        patientServiceStub.update.resolves(patientSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(patientServiceStub.update.calledWith(patientSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        patientServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PatientUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.patient = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(patientServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        patientServiceStub.find.resolves(patientSample);
        patientServiceStub.retrieve.resolves([patientSample]);

        // WHEN
        route = {
          params: {
            patientId: '' + patientSample.id,
          },
        };
        const wrapper = shallowMount(PatientUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.patient).toMatchObject(patientSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        patientServiceStub.find.resolves(patientSample);
        const wrapper = shallowMount(PatientUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
