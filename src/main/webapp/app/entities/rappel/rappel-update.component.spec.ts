/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import RappelUpdate from './rappel-update.vue';
import RappelService from './rappel.service';
import AlertService from '@/shared/alert/alert.service';

import PatientService from '@/entities/patient/patient.service';
import MedecinService from '@/entities/medecin/medecin.service';

type RappelUpdateComponentType = InstanceType<typeof RappelUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const rappelSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RappelUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Rappel Management Update Component', () => {
    let comp: RappelUpdateComponentType;
    let rappelServiceStub: SinonStubbedInstance<RappelService>;

    beforeEach(() => {
      route = {};
      rappelServiceStub = sinon.createStubInstance<RappelService>(RappelService);
      rappelServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          rappelService: () => rappelServiceStub,
          patientService: () =>
            sinon.createStubInstance<PatientService>(PatientService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          medecinService: () =>
            sinon.createStubInstance<MedecinService>(MedecinService, {
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
        const wrapper = shallowMount(RappelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.rappel = rappelSample;
        rappelServiceStub.update.resolves(rappelSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(rappelServiceStub.update.calledWith(rappelSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        rappelServiceStub.create.resolves(entity);
        const wrapper = shallowMount(RappelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.rappel = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(rappelServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        rappelServiceStub.find.resolves(rappelSample);
        rappelServiceStub.retrieve.resolves([rappelSample]);

        // WHEN
        route = {
          params: {
            rappelId: '' + rappelSample.id,
          },
        };
        const wrapper = shallowMount(RappelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.rappel).toMatchObject(rappelSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        rappelServiceStub.find.resolves(rappelSample);
        const wrapper = shallowMount(RappelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
