/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Alerte from './alerte.vue';
import AlerteService from './alerte.service';
import AlertService from '@/shared/alert/alert.service';

type AlerteComponentType = InstanceType<typeof Alerte>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Alerte Management Component', () => {
    let alerteServiceStub: SinonStubbedInstance<AlerteService>;
    let mountOptions: MountingOptions<AlerteComponentType>['global'];

    beforeEach(() => {
      alerteServiceStub = sinon.createStubInstance<AlerteService>(AlerteService);
      alerteServiceStub.retrieve.resolves({ headers: {} });

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
          alerteService: () => alerteServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        alerteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Alerte, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(alerteServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.alertes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AlerteComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Alerte, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        alerteServiceStub.retrieve.reset();
        alerteServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        alerteServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAlerte();
        await comp.$nextTick(); // clear components

        // THEN
        expect(alerteServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(alerteServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
