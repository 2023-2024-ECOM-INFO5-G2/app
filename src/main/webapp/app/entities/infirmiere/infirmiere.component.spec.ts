/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Infirmiere from './infirmiere.vue';
import InfirmiereService from './infirmiere.service';
import AlertService from '@/shared/alert/alert.service';

type InfirmiereComponentType = InstanceType<typeof Infirmiere>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Infirmiere Management Component', () => {
    let infirmiereServiceStub: SinonStubbedInstance<InfirmiereService>;
    let mountOptions: MountingOptions<InfirmiereComponentType>['global'];

    beforeEach(() => {
      infirmiereServiceStub = sinon.createStubInstance<InfirmiereService>(InfirmiereService);
      infirmiereServiceStub.retrieve.resolves({ headers: {} });

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
          infirmiereService: () => infirmiereServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        infirmiereServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Infirmiere, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(infirmiereServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.infirmieres[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: InfirmiereComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Infirmiere, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        infirmiereServiceStub.retrieve.reset();
        infirmiereServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        infirmiereServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeInfirmiere();
        await comp.$nextTick(); // clear components

        // THEN
        expect(infirmiereServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(infirmiereServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
