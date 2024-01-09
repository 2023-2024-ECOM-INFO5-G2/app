/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Mesure from './mesure.vue';
import MesureService from './mesure.service';
import AlertService from '@/shared/alert/alert.service';

type MesureComponentType = InstanceType<typeof Mesure>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Mesure Management Component', () => {
    let mesureServiceStub: SinonStubbedInstance<MesureService>;
    let mountOptions: MountingOptions<MesureComponentType>['global'];

    beforeEach(() => {
      mesureServiceStub = sinon.createStubInstance<MesureService>(MesureService);
      mesureServiceStub.retrieve.resolves({ headers: {} });

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
          mesureService: () => mesureServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesureServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Mesure, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mesureServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.mesures[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MesureComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Mesure, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        mesureServiceStub.retrieve.reset();
        mesureServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        mesureServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMesure();
        await comp.$nextTick(); // clear components

        // THEN
        expect(mesureServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(mesureServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
