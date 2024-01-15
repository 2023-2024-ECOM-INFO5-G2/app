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

describe('Alerte e2e test', () => {
  const alertePageUrl = '/alerte';
  const alertePageUrlPattern = new RegExp('/alerte(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const alerteSample = { description: 'pin-pon ding', date: '2023-12-18T23:24:34.191Z', severe: true };

  let alerte;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/alertes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/alertes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/alertes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (alerte) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/alertes/${alerte.id}`,
      }).then(() => {
        alerte = undefined;
      });
    }
  });

  it('Alertes menu should load Alertes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('alerte');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Alerte').should('exist');
    cy.url().should('match', alertePageUrlPattern);
  });

  describe('Alerte page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(alertePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Alerte page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/alerte/new$'));
        cy.getEntityCreateUpdateHeading('Alerte');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', alertePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/alertes',
          body: alerteSample,
        }).then(({ body }) => {
          alerte = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/alertes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [alerte],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(alertePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Alerte page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('alerte');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', alertePageUrlPattern);
      });

      it('edit button click should load edit Alerte page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Alerte');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', alertePageUrlPattern);
      });

      it('edit button click should load edit Alerte page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Alerte');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', alertePageUrlPattern);
      });

      it('last delete button click should delete instance of Alerte', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('alerte').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', alertePageUrlPattern);

        alerte = undefined;
      });
    });
  });

  describe('new Alerte page', () => {
    beforeEach(() => {
      cy.visit(`${alertePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Alerte');
    });

    it('should create an instance of Alerte', () => {
      cy.get(`[data-cy="description"]`).type('chut dessus');
      cy.get(`[data-cy="description"]`).should('have.value', 'chut dessus');

      cy.get(`[data-cy="date"]`).type('2023-12-19T07:07');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2023-12-19T07:07');

      cy.get(`[data-cy="severe"]`).should('not.be.checked');
      cy.get(`[data-cy="severe"]`).click();
      cy.get(`[data-cy="severe"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        alerte = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', alertePageUrlPattern);
    });
  });
});
