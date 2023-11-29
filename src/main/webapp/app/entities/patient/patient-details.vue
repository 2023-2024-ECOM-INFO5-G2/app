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
    <div class="col-12" v-if="albuPatient.length > 0">
      <div class="card">
        <div class="card-header">
          <div class="row align-items-center text-center">
            <div class="col"></div>
            <div class="col">
              <span>Albumine (g/kg)</span>
            </div>
            <div class="col">
              <b-button v-b-modal.modal-albu variant="primary">+</b-button>
            </div>
          </div>
        </div>
        <b-modal id="modal-albu" title="Ajouter une mesure d'Albumine" @ok="addAlbuValue">
          <b-form-input v-model="newAlbuValue" placeholder="Valeur mesurée (g/kg)" type="number"></b-form-input>
        </b-modal>
        <div class="card-body text-center">
          <h5>{{ albuPatient[albuPatient.length - 1].valeur }}</h5>
        </div>
      </div>
    </div>
  </div>
  <div class="row justify-content-center text-center mt-5">
    <div v-if="poidsPatient.length > 0" class="col-4">
      <div class="card">
        <div class="card-header">
          <div class="row align-items-center text-center">
            <div class="col"></div>
            <div class="col">
              <span>{{ t$('g2EcomApp.patient.poids') }}</span>
            </div>
            <div class="col">
              <b-button v-b-modal.modal-poids variant="primary">+</b-button>
            </div>
          </div>
        </div>
        <b-modal id="modal-poids" title="Ajouter une mesure de Poids" @ok="addPoidsValue">
          <b-form-input v-model="newWeightValue" placeholder="Valeur mesurée (kg)" type="number"></b-form-input>
        </b-modal>
        <div class="card-body">
          <h5>{{ poidsPatient[poidsPatient.length - 1].valeur }}</h5>
        </div>
      </div>
    </div>
    <div v-if="EPAPatient.length > 0" class="col-4">
      <div class="card">
        <div class="card-header">
          <div class="row align-items-center">
            <div class="col"></div>
            <div class="col">
              <span>{{ t$('g2EcomApp.patient.EPA') }}</span>
            </div>
            <div class="col">
              <b-button v-b-modal.modal-epa variant="primary">+</b-button>
            </div>
          </div>
        </div>

        <b-modal id="modal-epa" title="Ajouter une mesure EPA" @ok="addEPAValue">
          <b-form-input v-model="newEPAValue" placeholder="Valeur mesurée" type="number"></b-form-input>
        </b-modal>
        <div class="card-body">
          <h5>{{ EPAPatient[EPAPatient.length - 1].valeur }}</h5>
        </div>
      </div>
    </div>
    <div class="col-4">
      <div class="card">
        <div class="card-header py-3">
          <span>{{ t$('g2EcomApp.patient.IMC') }}</span>
        </div>
        <div class="card-body">
          <h5>{{ patientIMC }}</h5>
        </div>
      </div>
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

  <div class="row justify-content-center mt-5">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title" v-text="t$('g2EcomApp.patient.infoComplementaires')"></h4>
          <p class="card-text">
            {{ patient.infoComplementaires }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./patient-details.component.ts"></script>
