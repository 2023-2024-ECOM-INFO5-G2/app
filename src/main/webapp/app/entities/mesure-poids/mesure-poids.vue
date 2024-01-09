<template>
  <div>
    <h2 id="page-heading" data-cy="MesurePoidsHeading">
      <span v-text="t$('ecom02App.mesurePoids.home.title')" id="mesure-poids-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('ecom02App.mesurePoids.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MesurePoidsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-mesure-poids"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('ecom02App.mesurePoids.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mesurePoids && mesurePoids.length === 0">
      <span v-text="t$('ecom02App.mesurePoids.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="mesurePoids && mesurePoids.length > 0">
      <table class="table table-striped" aria-describedby="mesurePoids">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('ecom02App.mesurePoids.valeur')"></span></th>
            <th scope="row"><span v-text="t$('ecom02App.mesurePoids.date')"></span></th>
            <th scope="row"><span v-text="t$('ecom02App.mesurePoids.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mesurePoids in mesurePoids" :key="mesurePoids.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MesurePoidsView', params: { mesurePoidsId: mesurePoids.id } }">{{ mesurePoids.id }}</router-link>
            </td>
            <td>{{ mesurePoids.valeur }}</td>
            <td>{{ formatDateShort(mesurePoids.date) || '' }}</td>
            <td>
              <div v-if="mesurePoids.patient">
                <router-link :to="{ name: 'PatientView', params: { patientId: mesurePoids.patient.id } }">{{
                  mesurePoids.patient.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MesurePoidsView', params: { mesurePoidsId: mesurePoids.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MesurePoidsEdit', params: { mesurePoidsId: mesurePoids.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(mesurePoids)"
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
        <span id="ecom02App.mesurePoids.delete.question" data-cy="mesurePoidsDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-mesurePoids-heading" v-text="t$('ecom02App.mesurePoids.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-mesurePoids"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeMesurePoids()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./mesure-poids.component.ts"></script>
