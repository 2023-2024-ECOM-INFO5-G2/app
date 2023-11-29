<template>
  <div>
    <h2 id="page-heading" data-cy="MesureHeading">
      <span v-text="t$('g2EcomApp.mesure.home.title')" id="mesure-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2EcomApp.mesure.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MesureCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-mesure"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2EcomApp.mesure.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mesures && mesures.length === 0">
      <span v-text="t$('g2EcomApp.mesure.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="mesures && mesures.length > 0">
      <table class="table table-striped" aria-describedby="mesures">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.mesure.date')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.mesure.nomValeur')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.mesure.valeur')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.mesure.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mesure in mesures" :key="mesure.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MesureView', params: { mesureId: mesure.id } }">{{ mesure.id }}</router-link>
            </td>
            <td>{{ mesure.date }}</td>
            <td>{{ mesure.nomValeur }}</td>
            <td>{{ mesure.valeur }}</td>
            <td>
              <div v-if="mesure.patient">
                <router-link :to="{ name: 'PatientView', params: { patientId: mesure.patient.id } }">{{ mesure.patient.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MesureView', params: { mesureId: mesure.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MesureEdit', params: { mesureId: mesure.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(mesure)"
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
        <span id="g2EcomApp.mesure.delete.question" data-cy="mesureDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-mesure-heading" v-text="t$('g2EcomApp.mesure.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-mesure"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeMesure()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./mesure.component.ts"></script>
