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

describe('Mesure e2e test', () => {
  const mesurePageUrl = '/mesure';
  const mesurePageUrlPattern = new RegExp('/mesure(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mesureSample = { date: '2023-11-06', nomValeur: 'franco gratis', valeur: 8969.97 };

  let mesure;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mesures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mesures').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mesures/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mesure) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mesures/${mesure.id}`,
      }).then(() => {
        mesure = undefined;
      });
    }
  });

  it('Mesures menu should load Mesures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mesure');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Mesure').should('exist');
    cy.url().should('match', mesurePageUrlPattern);
  });

  describe('Mesure page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mesurePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Mesure page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mesure/new$'));
        cy.getEntityCreateUpdateHeading('Mesure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mesures',
          body: mesureSample,
        }).then(({ body }) => {
          mesure = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mesures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mesure],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mesurePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Mesure page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mesure');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });

      it('edit button click should load edit Mesure page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mesure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });

      it('edit button click should load edit Mesure page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mesure');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });

      it('last delete button click should delete instance of Mesure', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mesure').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);

        mesure = undefined;
      });
    });
  });

  describe('new Mesure page', () => {
    beforeEach(() => {
      cy.visit(`${mesurePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Mesure');
    });

    it('should create an instance of Mesure', () => {
      cy.get(`[data-cy="date"]`).type('2023-11-07');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2023-11-07');

      cy.get(`[data-cy="nomValeur"]`).type('derrière svelte');
      cy.get(`[data-cy="nomValeur"]`).should('have.value', 'derrière svelte');

      cy.get(`[data-cy="valeur"]`).type('26318.29');
      cy.get(`[data-cy="valeur"]`).should('have.value', '26318.29');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mesure = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mesurePageUrlPattern);
    });
  });
});
