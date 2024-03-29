/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import PatientService from './patient.service';
import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Patient } from '@/shared/model/patient.model';

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
  describe('Patient Service', () => {
    let service: PatientService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PatientService();
      currentDate = new Date();
      elemDefault = new Patient(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, currentDate, 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateDeNaissance: dayjs(currentDate).format(DATE_FORMAT),
            dateArrivee: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a Patient', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            dateDeNaissance: dayjs(currentDate).format(DATE_FORMAT),
            dateArrivee: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
            dateArrivee: currentDate,
          },
          returnedFromService,
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Patient', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Patient', async () => {
        const returnedFromService = Object.assign(
          {
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            sexe: 'BBBBBB',
            taille: 1,
            dateDeNaissance: dayjs(currentDate).format(DATE_FORMAT),
            numChambre: 1,
            dateArrivee: dayjs(currentDate).format(DATE_TIME_FORMAT),
            infoComplementaires: 'BBBBBB',
          },
          elemDefault,
        );

        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
            dateArrivee: currentDate,
          },
          returnedFromService,
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Patient', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Patient', async () => {
        const patchObject = Object.assign(
          {
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            sexe: 'BBBBBB',
            taille: 1,
            dateDeNaissance: dayjs(currentDate).format(DATE_FORMAT),
            dateArrivee: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          new Patient(),
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
            dateArrivee: currentDate,
          },
          returnedFromService,
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Patient', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Patient', async () => {
        const returnedFromService = Object.assign(
          {
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            sexe: 'BBBBBB',
            taille: 1,
            dateDeNaissance: dayjs(currentDate).format(DATE_FORMAT),
            numChambre: 1,
            dateArrivee: dayjs(currentDate).format(DATE_TIME_FORMAT),
            infoComplementaires: 'BBBBBB',
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            dateDeNaissance: currentDate,
            dateArrivee: currentDate,
          },
          returnedFromService,
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Patient', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Patient', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Patient', async () => {
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
