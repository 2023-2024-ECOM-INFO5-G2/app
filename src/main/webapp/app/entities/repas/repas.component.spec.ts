/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Repas from './repas.vue';
import RepasService from './repas.service';
import AlertService from '@/shared/alert/alert.service';

type RepasComponentType = InstanceType<typeof Repas>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Repas Management Component', () => {
    let repasServiceStub: SinonStubbedInstance<RepasService>;
    let mountOptions: MountingOptions<RepasComponentType>['global'];

    beforeEach(() => {
      repasServiceStub = sinon.createStubInstance<RepasService>(RepasService);
      repasServiceStub.retrieve.resolves({ headers: {} });

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
          repasService: () => repasServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        repasServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Repas, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(repasServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.repas[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: RepasComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Repas, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        repasServiceStub.retrieve.reset();
        repasServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        repasServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeRepas();
        await comp.$nextTick(); // clear components

        // THEN
        expect(repasServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(repasServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
