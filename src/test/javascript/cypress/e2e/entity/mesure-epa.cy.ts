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

describe('MesureEPA e2e test', () => {
  const mesureEPAPageUrl = '/mesure-epa';
  const mesureEPAPageUrlPattern = new RegExp('/mesure-epa(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mesureEPASample = { valeur: 6.18, date: '2023-12-18T13:47:56.865Z' };

  let mesureEPA;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mesure-epas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mesure-epas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mesure-epas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mesureEPA) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mesure-epas/${mesureEPA.id}`,
      }).then(() => {
        mesureEPA = undefined;
      });
    }
  });

  it('MesureEPAS menu should load MesureEPAS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mesure-epa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MesureEPA').should('exist');
    cy.url().should('match', mesureEPAPageUrlPattern);
  });

  describe('MesureEPA page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mesureEPAPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MesureEPA page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mesure-epa/new$'));
        cy.getEntityCreateUpdateHeading('MesureEPA');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureEPAPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mesure-epas',
          body: mesureEPASample,
        }).then(({ body }) => {
          mesureEPA = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mesure-epas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mesureEPA],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mesureEPAPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MesureEPA page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mesureEPA');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureEPAPageUrlPattern);
      });

      it('edit button click should load edit MesureEPA page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MesureEPA');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureEPAPageUrlPattern);
      });

      it('edit button click should load edit MesureEPA page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MesureEPA');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureEPAPageUrlPattern);
      });

      it('last delete button click should delete instance of MesureEPA', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mesureEPA').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesureEPAPageUrlPattern);

        mesureEPA = undefined;
      });
    });
  });

  describe('new MesureEPA page', () => {
    beforeEach(() => {
      cy.visit(`${mesureEPAPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MesureEPA');
    });

    it('should create an instance of MesureEPA', () => {
      cy.get(`[data-cy="valeur"]`).type('2.08');
      cy.get(`[data-cy="valeur"]`).should('have.value', '2.08');

      cy.get(`[data-cy="date"]`).type('2023-12-18T15:45');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2023-12-18T15:45');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mesureEPA = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mesureEPAPageUrlPattern);
    });
  });
});
