<template>
  <div>
    <h2 id="page-heading" data-cy="EtablissementHeading">
      <span v-text="t$('g2ecomApp.etablissement.home.title')" id="etablissement-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2ecomApp.etablissement.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'EtablissementCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-etablissement"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2ecomApp.etablissement.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && etablissements && etablissements.length === 0">
      <span v-text="t$('g2ecomApp.etablissement.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="etablissements && etablissements.length > 0">
      <table class="table table-striped" aria-describedby="etablissements">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.etablissement.nom')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.etablissement.adresse')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.etablissement.ville')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.etablissement.codePostal')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.etablissement.user')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="etablissement in etablissements" :key="etablissement.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EtablissementView', params: { etablissementId: etablissement.id } }">{{
                etablissement.id
              }}</router-link>
            </td>
            <td>{{ etablissement.nom }}</td>
            <td>{{ etablissement.adresse }}</td>
            <td>{{ etablissement.ville }}</td>
            <td>{{ etablissement.codePostal }}</td>
            <td>
              <span v-for="(user, i) in etablissement.users" :key="user.id"
                >{{ i > 0 ? ', ' : '' }}
                {{ user.id }}
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'EtablissementView', params: { etablissementId: etablissement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'EtablissementEdit', params: { etablissementId: etablissement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(etablissement)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="g2ecomApp.etablissement.delete.question"
          data-cy="etablissementDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-etablissement-heading" v-text="t$('g2ecomApp.etablissement.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-etablissement"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeEtablissement()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./etablissement.component.ts"></script>
