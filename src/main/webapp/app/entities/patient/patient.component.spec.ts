/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Patient from './patient.vue';
import PatientService from './patient.service';
import AlertService from '@/shared/alert/alert.service';

type PatientComponentType = InstanceType<typeof Patient>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Patient Management Component', () => {
    let patientServiceStub: SinonStubbedInstance<PatientService>;
    let mountOptions: MountingOptions<PatientComponentType>['global'];

    beforeEach(() => {
      patientServiceStub = sinon.createStubInstance<PatientService>(PatientService);
      patientServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          patientService: () => patientServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        patientServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Patient, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(patientServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.patients[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: PatientComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Patient, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        patientServiceStub.retrieve.reset();
        patientServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        patientServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePatient();
        await comp.$nextTick(); // clear components

        // THEN
        expect(patientServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(patientServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
