/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Etablissement from './etablissement.vue';
import EtablissementService from './etablissement.service';
import AlertService from '@/shared/alert/alert.service';

type EtablissementComponentType = InstanceType<typeof Etablissement>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Etablissement Management Component', () => {
    let etablissementServiceStub: SinonStubbedInstance<EtablissementService>;
    let mountOptions: MountingOptions<EtablissementComponentType>['global'];

    beforeEach(() => {
      etablissementServiceStub = sinon.createStubInstance<EtablissementService>(EtablissementService);
      etablissementServiceStub.retrieve.resolves({ headers: {} });

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
          etablissementService: () => etablissementServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        etablissementServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Etablissement, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(etablissementServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.etablissements[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: EtablissementComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Etablissement, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        etablissementServiceStub.retrieve.reset();
        etablissementServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        etablissementServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeEtablissement();
        await comp.$nextTick(); // clear components

        // THEN
        expect(etablissementServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(etablissementServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
