<template>
  <div class="row">
    <div class="col-md-11 py-1">
      <font-awesome-icon icon="user"></font-awesome-icon>
      <span class="h3">
        {{ patient.prenom }}
      </span>
      &nbsp;
      <span class="h3 text-uppercase">
        <strong>
          {{ patient.nom }}
        </strong>
      </span>
    </div>
    <div class="col-md-1">
      <router-link v-if="patient.id" v-slot="{ navigate }" :to="{ name: 'PatientEdit', params: { patientId: patient.id } }" custom>
        <button class="btn btn-primary" @click="navigate">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
        </button>
      </router-link>
    </div>
    <div class="col-6 py-1">
      <font-awesome-icon :icon="['fas', 'cake-candles']" />
      <span class="h6">{{ patient.dateDeNaissance }}</span>
    </div>
    <div class="col-6 py-1">
      <font-awesome-icon :icon="['fas', 'genderless']" />
      <span class="h6">{{ patient.sexe }}</span>
    </div>
    <div class="col-6 py-1">
      <font-awesome-icon :icon="['fas', 'location-dot']" />
      <span class="h6">Chambre {{ patient.numChambre }}</span>
    </div>
    <div class="col-6 py-1">
      <font-awesome-icon :icon="['fas', 'arrows-up-down']" />
      <span class="h6">{{ patient.taille }}cm</span>
    </div>
    <div class="col-6 py-1">
      <font-awesome-icon :icon="['fas', 'door-open']" />
      <span class="h6">{{ patient.dateArrivee }}</span>
    </div>
  </div>
  <div class="row justify-content-center mt-5">
    <div class="col-12">
      <b-card header="IMC" align="center">
        <b-card-title>
          {{ patientIMC || 'Aucune donnée' }}
        </b-card-title>
      </b-card>
    </div>
  </div>
  <div class="row justify-content-center mt-5">
    <div v-if="poidsPatient" class="col-4">
      <b-card
        :border-variant="dangerWeight ? 'danger' : ''"
        header="Poids (kg)"
        :header-bg-variant="dangerWeight ? 'danger' : ''"
        :header-text-variant="dangerWeight ? 'white' : ''"
        align="center"
      >
        <b-card-title>
          {{ poidsPatient[poidsPatient.length - 1]?.valeur || 'Aucune donnée' }}
        </b-card-title>

        <template #footer>
          <b-button v-b-modal.modal-poids variant="outline-primary">Ajouter une valeur</b-button>
          <b-modal id="modal-poids" title="Ajouter une mesure de Poids" @ok="addPoidsValue">
            <b-form-input v-model="newWeightValue" placeholder="Valeur mesurée (kg)" type="number"></b-form-input>
          </b-modal>
        </template>
      </b-card>
    </div>
    <div v-if="EPAPatient" class="col-4">
      <b-card
        :border-variant="dangerEPA ? 'danger' : ''"
        header="EPA"
        :header-bg-variant="dangerEPA ? 'danger' : ''"
        :header-text-variant="dangerEPA ? 'white' : ''"
        align="center"
      >
        <b-card-title>
          {{ EPAPatient[EPAPatient.length - 1]?.valeur || 'Aucune donnée' }}
        </b-card-title>

        <template #footer>
          <b-button v-b-modal.modal-epa variant="outline-primary">Ajouter une valeur</b-button>
          <b-modal id="modal-epa" title="Ajouter une mesure EPA" @ok="addEPAValue">
            <b-form-input v-model="newEPAValue" placeholder="Valeur mesurée" type="number"></b-form-input>
          </b-modal>
        </template>
      </b-card>
    </div>
    <div v-if="albuPatient" class="col-4">
      <b-card align="center" header="Albumine (g/kg)">
        <b-card-title>
          {{ albuPatient[albuPatient.length - 1]?.valeur || 'Aucune donnée' }}
        </b-card-title>

        <template #footer>
          <b-button v-b-modal.modal-albu variant="outline-primary">Ajouter une valeur</b-button>
          <b-modal id="modal-albu" title="Ajouter une mesure d'Albumine" @ok="addAlbuValue">
            <b-form-input v-model="newAlbuValue" placeholder="Valeur mesurée (g/kg)" type="number"></b-form-input>
          </b-modal>
        </template>
      </b-card>
    </div>
  </div>
  <div class="row justify-content-center text-center mt-5">
    <div v-if="weightChartLoaded" class="col">
      <Line id="my-chart-id" :data="weightChartData" :options="chartOptions" />
    </div>
    <div v-if="EPAChartLoaded" class="col">
      <Line id="my-chart-id" :data="EPAChartData" :options="chartOptions" />
    </div>
  </div>

  <div class="row mt-5">
    <div class="col-12">
      <h2>Repas</h2>
    </div>
    <div class="col-12">
      <b-table id="my-table" :current-page="tableCurrentPage" :items="patientMeals" :per-page="itemsPerPageTable" hover striped> </b-table>
    </div>
    <div class="col-12">
      <b-pagination
        v-if="patientMeals.length > itemsPerPageTable"
        v-model="tableCurrentPage"
        :per-page="itemsPerPageTable"
        :total-rows="patientMeals.length"
        aria-controls="my-table"
      ></b-pagination>
    </div>
    <div class="col-12">
      <b-button v-b-modal.modal-repas variant="primary">Ajouter un repas</b-button>
      <b-modal id="modal-repas" title="Ajouter un repas" @ok="addMeal">
        <b-form-input v-model="mealName" placeholder="Repas" type="text"></b-form-input>
        <b-form-input v-model="mealDesc" class="mt-2" placeholder="Description" type="text"></b-form-input>
        <b-form-input v-model="mealCal" class="mt-2" placeholder="Calories (kcal)" type="number"></b-form-input>
      </b-modal>
    </div>
  </div>

  <div class="row justify-content-center mt-5">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title" v-text="t$('g2ecomApp.patient.infosComplementaires')"></h4>
          <p class="card-text">
            {{ patient.infosComplementaires }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./patient-details.component.ts"></script>
