<template>
  <div class="home row">
    <div class="col-md-3">
      <h4 class="card-header">Liste des établissements</h4>
      <ul class="list-group">
        <li
          class="list-group-item"
          v-for="etablissement in etablissements"
          :key="etablissement.id"
          @click="() => selectEtablissement(etablissement)"
          :class="{ active: selectedetablissement === etablissement }"
        >
          {{ etablissement.nom }}
        </li>
      </ul>
    </div>

    <div class="col-md-9">
      <h1 class="display-4" v-text="t$('home.title')"></h1>

      <div>
        <div class="alert alert-success" v-if="authenticated">
          <span v-if="username" v-text="t$('home.logged.message', { username: username })"></span>
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          <span v-text="t$('global.messages.info.authenticated.prefix')"></span>
          <a class="alert-link" v-on:click="openLogin()" v-text="t$('global.messages.info.authenticated.link')"></a
          ><span v-html="t$('global.messages.info.authenticated.suffix')"></span>
        </div>
        <div class="alert alert-warning" v-if="!authenticated">
          <span v-text="t$('global.messages.info.register.noaccount')"></span>&nbsp;
          <router-link class="alert-link" to="/register" v-text="t$('global.messages.info.register.link')"></router-link>
        </div>
      </div>

      <div class="row justify-content-center text-center mt-5">
        <div class="col-4">
          <div class="card">
            <h6 class="card-header">{{ 'Etablissement choisi : ' + selectedetablissement.nom }}</h6>
            <div class="card-body">
              <h5>{{ selectedetablissement.ville + ' ' + selectedetablissement.adresse }}</h5>
            </div>
          </div>
        </div>
        <div class="col-4">
          <div class="card">
            <h6 class="card-header">{{ 'Cas détectés' }}</h6>
            <div class="card-body">
              <h5>{{ getAlertesCount() }}</h5>
            </div>
          </div>
        </div>
        <div class="col-4">
          <div class="card">
            <h6 class="card-header">Informations</h6>
            <div class="card-body">
              <h5>
                {{ patients?.filter(p => p.etablissement && p.etablissement.id === selectedetablissement.id).length }} patient(s) au sein de
                l'établissement
              </h5>
            </div>
          </div>
        </div>
      </div>

      <div class="row align-items-center">
        <div class="col-7 col-lg-3">
          <h1 class="my-4">Liste des patients</h1>
        </div>
        <div class="col-3">
          <router-link :to="{ name: 'PatientCreate' }" custom v-slot="{ navigate }">
            <button
              @click="navigate"
              id="jh-create-entity"
              data-cy="entityCreateButton"
              class="btn btn-primary jh-create-entity create-patient"
            >
              <font-awesome-icon icon="plus"></font-awesome-icon>
              <span> Ajouter un patient</span>
            </button>
          </router-link>
        </div>
      </div>
      <div class="row mt-5">
        <div class="col">
          <div style="max-height: 300px; overflow-y: auto">
            <table aria-describedby="patients" class="table table-striped table-hover">
              <thead>
                <tr>
                  <th scope="row"><span v-text="t$('ecom02App.patient.prenom')"></span></th>
                  <th scope="row"><span v-text="t$('ecom02App.patient.nom')"></span></th>
                  <th scope="row" v-if="alertes.length > 0"></th>
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
                    <td v-if="alertes.filter(a => a.patient.id === patient.id).length > 0">
                      <b-badge pill variant="danger">Alerte</b-badge>
                    </td>
                    <td v-else-if="alertes.length > 0"></td>
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
          </div>
        </div>
      </div>

      <div class="row justify-content-end">
        <div class="col-md-4">
          <div class="card text-center">
            <h6 class="card-header">Créer un rappel pour un patient</h6>
            <div class="card-body">
              <b-button v-b-modal.modal-instruction variant="primary">+</b-button>
            </div>
          </div>
        </div>
      </div>

      <b-modal id="modal-instruction" title="Créer un rappel" @ok="addInstruction">
        <b-form-input v-model="instructionDesc" placeholder="Description de l'instruction" type="text" class="mt-3"></b-form-input>
        <div class="mt-3">
          A réaliser le
          <b-form-input v-model="instructionEcheance" type="datetime-local"></b-form-input>
        </div>

        <b-form-input v-model="instructionInterv" placeholder="Intervalle de jours" type="number" class="mt-3"></b-form-input>
        <b-form-select v-model="instructionPatient" :options="instructionOptions" class="mt-3"></b-form-select>
      </b-modal>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>

<style>
.list-group-item {
  cursor: pointer;
}
</style>
