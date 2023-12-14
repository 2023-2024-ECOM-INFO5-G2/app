<template>
  <div>
    <h2 id="page-heading" data-cy="MesureEPAHeading">
      <span v-text="t$('g2ecomApp.mesureEPA.home.title')" id="mesure-epa-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2ecomApp.mesureEPA.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MesureEPACreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-mesure-epa"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2ecomApp.mesureEPA.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mesureEPAS && mesureEPAS.length === 0">
      <span v-text="t$('g2ecomApp.mesureEPA.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="mesureEPAS && mesureEPAS.length > 0">
      <table class="table table-striped" aria-describedby="mesureEPAS">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.mesureEPA.valeur')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.mesureEPA.date')"></span></th>
            <th scope="row"><span v-text="t$('g2ecomApp.mesureEPA.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mesureEPA in mesureEPAS" :key="mesureEPA.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MesureEPAView', params: { mesureEPAId: mesureEPA.id } }">{{ mesureEPA.id }}</router-link>
            </td>
            <td>{{ mesureEPA.valeur }}</td>
            <td>{{ formatDateShort(mesureEPA.date) || '' }}</td>
            <td>
              <div v-if="mesureEPA.patient">
                <router-link :to="{ name: 'PatientView', params: { patientId: mesureEPA.patient.id } }">{{
                  mesureEPA.patient.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MesureEPAView', params: { mesureEPAId: mesureEPA.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MesureEPAEdit', params: { mesureEPAId: mesureEPA.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(mesureEPA)"
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
        <span id="g2ecomApp.mesureEPA.delete.question" data-cy="mesureEPADeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-mesureEPA-heading" v-text="t$('g2ecomApp.mesureEPA.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-mesureEPA"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeMesureEPA()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./mesure-epa.component.ts"></script>
