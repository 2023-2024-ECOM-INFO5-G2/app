/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MesureEPA from './mesure-epa.vue';
import MesureEPAService from './mesure-epa.service';
import AlertService from '@/shared/alert/alert.service';

type MesureEPAComponentType = InstanceType<typeof MesureEPA>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MesureEPA Management Component', () => {
    let mesureEPAServiceStub: SinonStubbedInstance<MesureEPAService>;
    let mountOptions: MountingOptions<MesureEPAComponentType>['global'];

    beforeEach(() => {
      mesureEPAServiceStub = sinon.createStubInstance<MesureEPAService>(MesureEPAService);
      mesureEPAServiceStub.retrieve.resolves({ headers: {} });

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
          mesureEPAService: () => mesureEPAServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesureEPAServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MesureEPA, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mesureEPAServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.mesureEPAS[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MesureEPAComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MesureEPA, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        mesureEPAServiceStub.retrieve.reset();
        mesureEPAServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        mesureEPAServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMesureEPA();
        await comp.$nextTick(); // clear components

        // THEN
        expect(mesureEPAServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(mesureEPAServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
