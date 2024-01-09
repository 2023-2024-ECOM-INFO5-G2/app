/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Rappel from './rappel.vue';
import RappelService from './rappel.service';
import AlertService from '@/shared/alert/alert.service';

type RappelComponentType = InstanceType<typeof Rappel>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Rappel Management Component', () => {
    let rappelServiceStub: SinonStubbedInstance<RappelService>;
    let mountOptions: MountingOptions<RappelComponentType>['global'];

    beforeEach(() => {
      rappelServiceStub = sinon.createStubInstance<RappelService>(RappelService);
      rappelServiceStub.retrieve.resolves({ headers: {} });

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
          rappelService: () => rappelServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        rappelServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Rappel, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(rappelServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.rappels[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: RappelComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Rappel, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        rappelServiceStub.retrieve.reset();
        rappelServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        rappelServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeRappel();
        await comp.$nextTick(); // clear components

        // THEN
        expect(rappelServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(rappelServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
