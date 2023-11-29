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

describe('Infirmiere e2e test', () => {
  const infirmierePageUrl = '/infirmiere';
  const infirmierePageUrlPattern = new RegExp('/infirmiere(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const infirmiereSample = {};

  let infirmiere;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/infirmieres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/infirmieres').as('postEntityRequest');
    cy.intercept('DELETE', '/api/infirmieres/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (infirmiere) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/infirmieres/${infirmiere.id}`,
      }).then(() => {
        infirmiere = undefined;
      });
    }
  });

  it('Infirmieres menu should load Infirmieres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('infirmiere');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Infirmiere').should('exist');
    cy.url().should('match', infirmierePageUrlPattern);
  });

  describe('Infirmiere page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(infirmierePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Infirmiere page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/infirmiere/new$'));
        cy.getEntityCreateUpdateHeading('Infirmiere');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infirmierePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/infirmieres',
          body: infirmiereSample,
        }).then(({ body }) => {
          infirmiere = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/infirmieres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [infirmiere],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(infirmierePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Infirmiere page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('infirmiere');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infirmierePageUrlPattern);
      });

      it('edit button click should load edit Infirmiere page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Infirmiere');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infirmierePageUrlPattern);
      });

      it('edit button click should load edit Infirmiere page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Infirmiere');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infirmierePageUrlPattern);
      });

      it('last delete button click should delete instance of Infirmiere', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('infirmiere').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infirmierePageUrlPattern);

        infirmiere = undefined;
      });
    });
  });

  describe('new Infirmiere page', () => {
    beforeEach(() => {
      cy.visit(`${infirmierePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Infirmiere');
    });

    it('should create an instance of Infirmiere', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        infirmiere = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', infirmierePageUrlPattern);
    });
  });
});
