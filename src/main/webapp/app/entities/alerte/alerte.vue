<template>
  <div>
    <h2 id="page-heading" data-cy="AlerteHeading">
      <span v-text="t$('g2EcomApp.alerte.home.title')" id="alerte-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2EcomApp.alerte.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AlerteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-alerte"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2EcomApp.alerte.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && alertes && alertes.length === 0">
      <span v-text="t$('g2EcomApp.alerte.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="alertes && alertes.length > 0">
      <table class="table table-striped" aria-describedby="alertes">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.alerte.description')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.alerte.date')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.alerte.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="alerte in alertes" :key="alerte.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AlerteView', params: { alerteId: alerte.id } }">{{ alerte.id }}</router-link>
            </td>
            <td>{{ alerte.description }}</td>
            <td>{{ alerte.date }}</td>
            <td>
              <span v-for="(patient, i) in alerte.patients" :key="patient.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'PatientView', params: { patientId: patient.id } }">{{
                  patient.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AlerteView', params: { alerteId: alerte.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AlerteEdit', params: { alerteId: alerte.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(alerte)"
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
        <span id="g2EcomApp.alerte.delete.question" data-cy="alerteDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-alerte-heading" v-text="t$('g2EcomApp.alerte.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-alerte"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeAlerte()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./alerte.component.ts"></script>