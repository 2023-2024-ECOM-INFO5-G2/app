import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('MesureAlbumine e2e test', () => {
  const mesureAlbuminePageUrl = '/mesure-albumine';
  const mesureAlbuminePageUrlPattern = new RegExp('/mesure-albumine(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mesureAlbumineSample = { valeur: 74.21, date: '2023-12-18T13:07:18.545Z' };

  let mesureAlbumine;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mesure-albumines+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mesure-albumines').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mesure-albumines/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mesureAlbumine) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mesure-albumines/${mesureAlbumine.id}`,
      }).then(() => {
        mesureAlbumine = undefined;
      });
    }
  });

  it('MesureAlbumines menu should load MesureAlbumines page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mesure-albumine');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MesureAlbumine').should('exist');
    cy.url().should('match', mesureAlbuminePageUrlPattern);
  });

  describe('MesureAlbumine page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mesureAlbuminePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MesureAlbumine page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mesure-albumine/new$'));
        cy.getEntityCreateUpdateHeading('MesureAlbumine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureAlbuminePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mesure-albumines',
          body: mesureAlbumineSample,
        }).then(({ body }) => {
          mesureAlbumine = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mesure-albumines+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mesureAlbumine],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mesureAlbuminePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MesureAlbumine page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mesureAlbumine');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureAlbuminePageUrlPattern);
      });

      it('edit button click should load edit MesureAlbumine page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MesureAlbumine');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureAlbuminePageUrlPattern);
      });

      it('edit button click should load edit MesureAlbumine page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MesureAlbumine');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureAlbuminePageUrlPattern);
      });

      it('last delete button click should delete instance of MesureAlbumine', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mesureAlbumine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureAlbuminePageUrlPattern);

        mesureAlbumine = undefined;
      });
    });
  });

  describe('new MesureAlbumine page', () => {
    beforeEach(() => {
      cy.visit(`${mesureAlbuminePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MesureAlbumine');
    });

    it('should create an instance of MesureAlbumine', () => {
      cy.get(`[data-cy="valeur"]`).type('56.66');
      cy.get(`[data-cy="valeur"]`).should('have.value', '56.66');

      cy.get(`[data-cy="date"]`).type('2023-12-19T07:17');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2023-12-19T07:17');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mesureAlbumine = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mesureAlbuminePageUrlPattern);
    });
  });
});
