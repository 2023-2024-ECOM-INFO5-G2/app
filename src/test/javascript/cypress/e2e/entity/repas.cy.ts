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

describe('Repas e2e test', () => {
  const repasPageUrl = '/repas';
  const repasPageUrlPattern = new RegExp('/repas(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const repasSample = { nom: 'au dépens de quitte à', date: '2023-12-04T15:49:48.860Z' };

  let repas;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/repas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/repas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/repas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (repas) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/repas/${repas.id}`,
      }).then(() => {
        repas = undefined;
      });
    }
  });

  it('Repas menu should load Repas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('repas');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Repas').should('exist');
    cy.url().should('match', repasPageUrlPattern);
  });

  describe('Repas page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(repasPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Repas page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/repas/new$'));
        cy.getEntityCreateUpdateHeading('Repas');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', repasPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/repas',
          body: repasSample,
        }).then(({ body }) => {
          repas = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/repas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [repas],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(repasPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Repas page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('repas');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', repasPageUrlPattern);
      });

      it('edit button click should load edit Repas page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Repas');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', repasPageUrlPattern);
      });

      it('edit button click should load edit Repas page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Repas');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', repasPageUrlPattern);
      });

      it('last delete button click should delete instance of Repas', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('repas').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', repasPageUrlPattern);

        repas = undefined;
      });
    });
  });

  describe('new Repas page', () => {
    beforeEach(() => {
      cy.visit(`${repasPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Repas');
    });

    it('should create an instance of Repas', () => {
      cy.get(`[data-cy="nom"]`).type('conseil municipal apparemment dans');
      cy.get(`[data-cy="nom"]`).should('have.value', 'conseil municipal apparemment dans');

      cy.get(`[data-cy="date"]`).type('2023-12-05T00:50');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2023-12-05T00:50');

      cy.get(`[data-cy="apportCalorique"]`).type('120.84');
      cy.get(`[data-cy="apportCalorique"]`).should('have.value', '120.84');

      cy.get(`[data-cy="poidsConsomme"]`).type('120.47');
      cy.get(`[data-cy="poidsConsomme"]`).should('have.value', '120.47');

      cy.get(`[data-cy="description"]`).type('horrible');
      cy.get(`[data-cy="description"]`).should('have.value', 'horrible');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        repas = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', repasPageUrlPattern);
    });
  });
});
