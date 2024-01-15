<template>
  <div class="row">
    <div class="col">
      <div class="card">
        <h6 class="card-header">Etablissement</h6>
        <select v-model="selectedetablissement" class="form-select">
          <option v-for="etablissement in etablissements" :value="etablissement" :key="etablissement.id">
            {{ etablissement.nom }}
          </option>
        </select>
        <div class="card-body">
          <h5>{{ selectedetablissement.nom }}</h5>
          <h5>{{ selectedetablissement.adresse + ' ' + selectedetablissement.ville }}</h5>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <h6 class="card-header" v-text="'Cas détectés'"></h6>
        <div class="card-body text-center">
          <h5><strong>2</strong></h5>
        </div>
      </div>
    </div>
  </div>
  <div class="row mt-5">
    <div class="col">
      <h2 id="page-heading" data-cy="PatientHeading">
        <span id="patient-heading" v-text="t$('ecom02App.patient.home.title')"></span>
      </h2>
    </div>
  </div>
  <div class="row mt-5">
    <div class="col">
      <table
        aria-describedby="patients"
        class="table table-striped table-hover"
        v-if="patients?.filter(p => p.etablissement && p.etablissement.id === selectedetablissement.id).length > 0"
      >
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('ecom02App.patient.prenom')"></span></th>
            <th scope="row"><span v-text="t$('ecom02App.patient.nom')"></span></th>
            <th scope="row"><span v-text="t$('ecom02App.patient.numChambre')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <template
            v-for="patient in patients?.filter(p => p.etablissement && p.etablissement.id === selectedetablissement.id)"
            :key="patient.id"
          >
            <tr data-cy="entityTable">
              <td>{{ patient.prenom }}</td>
              <td>{{ patient.nom }}</td>
              <td>{{ patient.numChambre }}</td>
              <td>
                <router-link v-slot="{ navigate }" :to="{ name: 'PatientView', params: { patientId: patient.id } }" custom>
                  <button class="btn btn-info btn-sm details" data-cy="entityDetailsButton" @click="navigate">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
      <div class="alert alert-warning" v-else>
        <span v-text="t$('ecom02App.patient.home.notFound')"></span>
      </div>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="ecom02App.patient.delete.question" data-cy="patientDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-patient-heading" v-text="t$('ecom02App.patient.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-patient"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removePatient()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./patient.component.ts"></script>
