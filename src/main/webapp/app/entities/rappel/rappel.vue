<template>
  <div>
    <h2 id="page-heading" data-cy="RappelHeading">
      <span v-text="t$('g2EcomApp.rappel.home.title')" id="rappel-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2EcomApp.rappel.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'RappelCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-rappel"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2EcomApp.rappel.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && rappels && rappels.length === 0">
      <span v-text="t$('g2EcomApp.rappel.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="rappels && rappels.length > 0">
      <table class="table table-striped" aria-describedby="rappels">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.rappel.frequence')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.rappel.echeance')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.rappel.tache')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.rappel.patient')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.rappel.medecin')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="rappel in rappels" :key="rappel.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RappelView', params: { rappelId: rappel.id } }">{{ rappel.id }}</router-link>
            </td>
            <td>{{ rappel.frequence }}</td>
            <td>{{ rappel.echeance }}</td>
            <td>{{ rappel.tache }}</td>
            <td>
              <div v-if="rappel.patient">
                <router-link :to="{ name: 'PatientView', params: { patientId: rappel.patient.id } }">{{ rappel.patient.id }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(medecin, i) in rappel.medecins" :key="medecin.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'MedecinView', params: { medecinId: medecin.id } }">{{
                  medecin.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'RappelView', params: { rappelId: rappel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'RappelEdit', params: { rappelId: rappel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(rappel)"
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
        <span id="g2EcomApp.rappel.delete.question" data-cy="rappelDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-rappel-heading" v-text="t$('g2EcomApp.rappel.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-rappel"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeRappel()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./rappel.component.ts"></script>
