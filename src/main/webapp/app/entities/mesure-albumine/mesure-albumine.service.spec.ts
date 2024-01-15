/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import MesureAlbumineService from './mesure-albumine.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { MesureAlbumine } from '@/shared/model/mesure-albumine.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('MesureAlbumine Service', () => {
    let service: MesureAlbumineService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new MesureAlbumineService();
      currentDate = new Date();
      elemDefault = new MesureAlbumine(123, 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault,
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a MesureAlbumine', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService,
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a MesureAlbumine', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a MesureAlbumine', async () => {
        const returnedFromService = Object.assign(
          {
            valeur: 1,
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault,
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService,
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a MesureAlbumine', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a MesureAlbumine', async () => {
        const patchObject = Object.assign(
          {
            valeur: 1,
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          new MesureAlbumine(),
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService,
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a MesureAlbumine', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of MesureAlbumine', async () => {
        const returnedFromService = Object.assign(
          {
            valeur: 1,
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService,
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of MesureAlbumine', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a MesureAlbumine', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a MesureAlbumine', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
