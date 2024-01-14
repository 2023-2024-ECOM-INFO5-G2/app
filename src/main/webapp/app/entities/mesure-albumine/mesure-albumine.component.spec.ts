/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MesureAlbumine from './mesure-albumine.vue';
import MesureAlbumineService from './mesure-albumine.service';
import AlertService from '@/shared/alert/alert.service';

type MesureAlbumineComponentType = InstanceType<typeof MesureAlbumine>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MesureAlbumine Management Component', () => {
    let mesureAlbumineServiceStub: SinonStubbedInstance<MesureAlbumineService>;
    let mountOptions: MountingOptions<MesureAlbumineComponentType>['global'];

    beforeEach(() => {
      mesureAlbumineServiceStub = sinon.createStubInstance<MesureAlbumineService>(MesureAlbumineService);
      mesureAlbumineServiceStub.retrieve.resolves({ headers: {} });

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
          mesureAlbumineService: () => mesureAlbumineServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mesureAlbumineServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MesureAlbumine, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mesureAlbumineServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.mesureAlbumines[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MesureAlbumineComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MesureAlbumine, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        mesureAlbumineServiceStub.retrieve.reset();
        mesureAlbumineServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        mesureAlbumineServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMesureAlbumine();
        await comp.$nextTick(); // clear components

        // THEN
        expect(mesureAlbumineServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(mesureAlbumineServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
