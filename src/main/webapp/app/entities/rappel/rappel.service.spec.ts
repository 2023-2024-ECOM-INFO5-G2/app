/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import RappelService from './rappel.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Rappel } from '@/shared/model/rappel.model';

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
  describe('Rappel Service', () => {
    let service: RappelService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new RappelService();
      currentDate = new Date();
      elemDefault = new Rappel(123, currentDate, currentDate, 0, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            echeance: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a Rappel', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            echeance: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            date: currentDate,
            echeance: currentDate,
          },
          returnedFromService,
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Rappel', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Rappel', async () => {
        const returnedFromService = Object.assign(
          {
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            echeance: dayjs(currentDate).format(DATE_TIME_FORMAT),
            intervaleJours: 1,
            tache: 'BBBBBB',
            feeDansLetang: true,
          },
          elemDefault,
        );

        const expected = Object.assign(
          {
            date: currentDate,
            echeance: currentDate,
          },
          returnedFromService,
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Rappel', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Rappel', async () => {
        const patchObject = Object.assign(
          {
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            intervaleJours: 1,
            tache: 'BBBBBB',
          },
          new Rappel(),
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            echeance: currentDate,
          },
          returnedFromService,
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Rappel', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Rappel', async () => {
        const returnedFromService = Object.assign(
          {
            date: dayjs(currentDate).format(DATE_TIME_FORMAT),
            echeance: dayjs(currentDate).format(DATE_TIME_FORMAT),
            intervaleJours: 1,
            tache: 'BBBBBB',
            feeDansLetang: true,
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            date: currentDate,
            echeance: currentDate,
          },
          returnedFromService,
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Rappel', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Rappel', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Rappel', async () => {
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
