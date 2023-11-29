/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Medecin from './medecin.vue';
import MedecinService from './medecin.service';
import AlertService from '@/shared/alert/alert.service';

type MedecinComponentType = InstanceType<typeof Medecin>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Medecin Management Component', () => {
    let medecinServiceStub: SinonStubbedInstance<MedecinService>;
    let mountOptions: MountingOptions<MedecinComponentType>['global'];

    beforeEach(() => {
      medecinServiceStub = sinon.createStubInstance<MedecinService>(MedecinService);
      medecinServiceStub.retrieve.resolves({ headers: {} });

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
          medecinService: () => medecinServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        medecinServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Medecin, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(medecinServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.medecins[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MedecinComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Medecin, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        medecinServiceStub.retrieve.reset();
        medecinServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        medecinServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMedecin();
        await comp.$nextTick(); // clear components

        // THEN
        expect(medecinServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(medecinServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
