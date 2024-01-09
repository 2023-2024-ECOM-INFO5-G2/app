/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InfirmiereUpdate from './infirmiere-update.vue';
import InfirmiereService from './infirmiere.service';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import PatientService from '@/entities/patient/patient.service';

type InfirmiereUpdateComponentType = InstanceType<typeof InfirmiereUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const infirmiereSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<InfirmiereUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Infirmiere Management Update Component', () => {
    let comp: InfirmiereUpdateComponentType;
    let infirmiereServiceStub: SinonStubbedInstance<InfirmiereService>;

    beforeEach(() => {
      route = {};
      infirmiereServiceStub = sinon.createStubInstance<InfirmiereService>(InfirmiereService);
      infirmiereServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          infirmiereService: () => infirmiereServiceStub,

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
        const wrapper = shallowMount(InfirmiereUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.infirmiere = infirmiereSample;
        infirmiereServiceStub.update.resolves(infirmiereSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(infirmiereServiceStub.update.calledWith(infirmiereSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        infirmiereServiceStub.create.resolves(entity);
        const wrapper = shallowMount(InfirmiereUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.infirmiere = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(infirmiereServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        infirmiereServiceStub.find.resolves(infirmiereSample);
        infirmiereServiceStub.retrieve.resolves([infirmiereSample]);

        // WHEN
        route = {
          params: {
            infirmiereId: '' + infirmiereSample.id,
          },
        };
        const wrapper = shallowMount(InfirmiereUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.infirmiere).toMatchObject(infirmiereSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        infirmiereServiceStub.find.resolves(infirmiereSample);
        const wrapper = shallowMount(InfirmiereUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
