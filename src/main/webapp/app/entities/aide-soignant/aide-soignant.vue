<template>
  <div>
    <h2 id="page-heading" data-cy="AideSoignantHeading">
      <span v-text="t$('g2EcomApp.aideSoignant.home.title')" id="aide-soignant-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('g2EcomApp.aideSoignant.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AideSoignantCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-aide-soignant"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('g2EcomApp.aideSoignant.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && aideSoignants && aideSoignants.length === 0">
      <span v-text="t$('g2EcomApp.aideSoignant.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="aideSoignants && aideSoignants.length > 0">
      <table class="table table-striped" aria-describedby="aideSoignants">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.aideSoignant.user')"></span></th>
            <th scope="row"><span v-text="t$('g2EcomApp.aideSoignant.patient')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="aideSoignant in aideSoignants" :key="aideSoignant.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AideSoignantView', params: { aideSoignantId: aideSoignant.id } }">{{
                aideSoignant.id
              }}</router-link>
            </td>
            <td>
              <span v-for="(user, i) in aideSoignant.users" :key="user.id"
                >{{ i > 0 ? ', ' : '' }}
                {{ user.id }}
              </span>
            </td>
            <td>
              <span v-for="(patient, i) in aideSoignant.patients" :key="patient.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'PatientView', params: { patientId: patient.id } }">{{
                  patient.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AideSoignantView', params: { aideSoignantId: aideSoignant.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AideSoignantEdit', params: { aideSoignantId: aideSoignant.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(aideSoignant)"
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
          id="g2EcomApp.aideSoignant.delete.question"
          data-cy="aideSoignantDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-aideSoignant-heading" v-text="t$('g2EcomApp.aideSoignant.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-aideSoignant"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeAideSoignant()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./aide-soignant.component.ts"></script>
