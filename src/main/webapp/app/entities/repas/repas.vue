<template>
  <div>
    <h2 id="page-heading" data-cy="RepasHeading">
      <span v-text="t$('g2EcomApp.repas.home.title')" id="repas-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2EcomApp.repas.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'RepasCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-repas"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2EcomApp.repas.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && repas && repas.length === 0">
      <span v-text="t$('g2EcomApp.repas.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="repas && repas.length > 0">
      <table class="table table-striped" aria-describedby="repas">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.repas.nom')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.repas.description')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.repas.apportCalorique')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.repas.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="repas in repas" :key="repas.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RepasView', params: { repasId: repas.id } }">{{ repas.id }}</router-link>
            </td>
            <td>{{ repas.nom }}</td>
            <td>{{ repas.description }}</td>
            <td>{{ repas.apportCalorique }}</td>
            <td>
              <span v-for="(patient, i) in repas.patients" :key="patient.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'PatientView', params: { patientId: patient.id } }">{{
                  patient.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'RepasView', params: { repasId: repas.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'RepasEdit', params: { repasId: repas.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(repas)"
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
        <span id="g2EcomApp.repas.delete.question" data-cy="repasDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-repas-heading" v-text="t$('g2EcomApp.repas.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-repas"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeRepas()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./repas.component.ts"></script>
