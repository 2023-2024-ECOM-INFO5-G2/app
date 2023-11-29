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

describe('AideSoignant e2e test', () => {
  const aideSoignantPageUrl = '/aide-soignant';
  const aideSoignantPageUrlPattern = new RegExp('/aide-soignant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const aideSoignantSample = {};

  let aideSoignant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aide-soignants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aide-soignants').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aide-soignants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aideSoignant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aide-soignants/${aideSoignant.id}`,
      }).then(() => {
        aideSoignant = undefined;
      });
    }
  });

  it('AideSoignants menu should load AideSoignants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aide-soignant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AideSoignant').should('exist');
    cy.url().should('match', aideSoignantPageUrlPattern);
  });

  describe('AideSoignant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aideSoignantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AideSoignant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/aide-soignant/new$'));
        cy.getEntityCreateUpdateHeading('AideSoignant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aideSoignantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aide-soignants',
          body: aideSoignantSample,
        }).then(({ body }) => {
          aideSoignant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aide-soignants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aideSoignant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aideSoignantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AideSoignant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aideSoignant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aideSoignantPageUrlPattern);
      });

      it('edit button click should load edit AideSoignant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AideSoignant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aideSoignantPageUrlPattern);
      });

      it('edit button click should load edit AideSoignant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AideSoignant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aideSoignantPageUrlPattern);
      });

      it('last delete button click should delete instance of AideSoignant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('aideSoignant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aideSoignantPageUrlPattern);

        aideSoignant = undefined;
      });
    });
  });

  describe('new AideSoignant page', () => {
    beforeEach(() => {
      cy.visit(`${aideSoignantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AideSoignant');
    });

    it('should create an instance of AideSoignant', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        aideSoignant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', aideSoignantPageUrlPattern);
    });
  });
});
