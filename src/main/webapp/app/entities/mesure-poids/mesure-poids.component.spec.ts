/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MesurePoids from './mesure-poids.vue';
import MesurePoidsService from './mesure-poids.service';
import AlertService from '@/shared/alert/alert.service';

type MesurePoidsComponentType = InstanceType<typeof MesurePoids>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MesurePoids Management Component', () => {
    let mesurePoidsServiceStub: SinonStubbedInstance<MesurePoidsService>;
    let mountOptions: MountingOptions<MesurePoidsComponentType>['global'];

    beforeEach(() => {
      mesurePoidsServiceStub = sinon.createStubInstance<MesurePoidsService>(MesurePoidsService);
      mesurePoidsServiceStub.retrieve.resolves({ headers: {} });

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
          mesurePoidsService: () => mesurePoidsServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesurePoidsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MesurePoids, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mesurePoidsServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.mesurePoids[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MesurePoidsComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MesurePoids, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        mesurePoidsServiceStub.retrieve.reset();
        mesurePoidsServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        mesurePoidsServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMesurePoids();
        await comp.$nextTick(); // clear components

        // THEN
        expect(mesurePoidsServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(mesurePoidsServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
