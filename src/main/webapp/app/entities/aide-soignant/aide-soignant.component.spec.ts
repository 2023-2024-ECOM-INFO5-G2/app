/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AideSoignant from './aide-soignant.vue';
import AideSoignantService from './aide-soignant.service';
import AlertService from '@/shared/alert/alert.service';

type AideSoignantComponentType = InstanceType<typeof AideSoignant>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AideSoignant Management Component', () => {
    let aideSoignantServiceStub: SinonStubbedInstance<AideSoignantService>;
    let mountOptions: MountingOptions<AideSoignantComponentType>['global'];

    beforeEach(() => {
      aideSoignantServiceStub = sinon.createStubInstance<AideSoignantService>(AideSoignantService);
      aideSoignantServiceStub.retrieve.resolves({ headers: {} });

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
          aideSoignantService: () => aideSoignantServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        aideSoignantServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AideSoignant, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(aideSoignantServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.aideSoignants[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AideSoignantComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AideSoignant, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        aideSoignantServiceStub.retrieve.reset();
        aideSoignantServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        aideSoignantServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAideSoignant();
        await comp.$nextTick(); // clear components

        // THEN
        expect(aideSoignantServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(aideSoignantServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
