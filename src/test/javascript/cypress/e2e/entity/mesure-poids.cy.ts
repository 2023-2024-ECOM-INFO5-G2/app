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

describe('MesurePoids e2e test', () => {
  const mesurePoidsPageUrl = '/mesure-poids';
  const mesurePoidsPageUrlPattern = new RegExp('/mesure-poids(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mesurePoidsSample = { valeur: 370.54, date: '2023-12-19T07:36:40.352Z' };

  let mesurePoids;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mesure-poids+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mesure-poids').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mesure-poids/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mesurePoids) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mesure-poids/${mesurePoids.id}`,
      }).then(() => {
        mesurePoids = undefined;
      });
    }
  });

  it('MesurePoids menu should load MesurePoids page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mesure-poids');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MesurePoids').should('exist');
    cy.url().should('match', mesurePoidsPageUrlPattern);
  });

  describe('MesurePoids page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mesurePoidsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MesurePoids page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mesure-poids/new$'));
        cy.getEntityCreateUpdateHeading('MesurePoids');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePoidsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mesure-poids',
          body: mesurePoidsSample,
        }).then(({ body }) => {
          mesurePoids = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mesure-poids+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mesurePoids],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mesurePoidsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MesurePoids page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mesurePoids');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePoidsPageUrlPattern);
      });

      it('edit button click should load edit MesurePoids page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MesurePoids');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePoidsPageUrlPattern);
      });

      it('edit button click should load edit MesurePoids page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MesurePoids');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePoidsPageUrlPattern);
      });

      it('last delete button click should delete instance of MesurePoids', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mesurePoids').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePoidsPageUrlPattern);

        mesurePoids = undefined;
      });
    });
  });

  describe('new MesurePoids page', () => {
    beforeEach(() => {
      cy.visit(`${mesurePoidsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MesurePoids');
    });

    it('should create an instance of MesurePoids', () => {
      cy.get(`[data-cy="valeur"]`).type('458.29');
      cy.get(`[data-cy="valeur"]`).should('have.value', '458.29');

      cy.get(`[data-cy="date"]`).type('2023-12-19T11:08');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2023-12-19T11:08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mesurePoids = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mesurePoidsPageUrlPattern);
    });
  });
});
