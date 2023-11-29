<template>
  <div>
    <h2 id="page-heading" data-cy="InfirmiereHeading">
      <span v-text="t$('g2EcomApp.infirmiere.home.title')" id="infirmiere-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2EcomApp.infirmiere.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'InfirmiereCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-infirmiere"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2EcomApp.infirmiere.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && infirmieres && infirmieres.length === 0">
      <span v-text="t$('g2EcomApp.infirmiere.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="infirmieres && infirmieres.length > 0">
      <table class="table table-striped" aria-describedby="infirmieres">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.infirmiere.user')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.infirmiere.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="infirmiere in infirmieres" :key="infirmiere.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InfirmiereView', params: { infirmiereId: infirmiere.id } }">{{ infirmiere.id }}</router-link>
            </td>
            <td>
              <span v-for="(user, i) in infirmiere.users" :key="user.id"
                >{{ i > 0 ? ', ' : '' }}
                {{ user.id }}
              </span>
            </td>
            <td>
              <span v-for="(patient, i) in infirmiere.patients" :key="patient.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'PatientView', params: { patientId: patient.id } }">{{
                  patient.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'InfirmiereView', params: { infirmiereId: infirmiere.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'InfirmiereEdit', params: { infirmiereId: infirmiere.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(infirmiere)"
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
        <span id="g2EcomApp.infirmiere.delete.question" data-cy="infirmiereDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-infirmiere-heading" v-text="t$('g2EcomApp.infirmiere.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-infirmiere"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeInfirmiere()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./infirmiere.component.ts"></script>
