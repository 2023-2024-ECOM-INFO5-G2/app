<template>
  <div class="row">
    <div class="col-9 col-md-10 py-1 col-lg-11">
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
    <div class="col-3 col-md-2 col-lg-1">
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
      <span class="h6">{{ new Date(patient.dateArrivee).toLocaleDateString() }}</span>
    </div>
  </div>
  <div class="row justify-content-center mt-5">
    <div class="col-lg-6 col-12">
      <div v-if="chartDataLoaded">
        <Line id="my-chart-id" :data="chartData" :options="chartOptions" />
      </div>
    </div>
    <div class="col-lg-6 col-12">
      <div class="row justify-content-center">
        <div class="col-6">
          <b-card align="center" header="IMC">
            <b-card-title>
              {{ patientIMC || 'Aucune donnée' }}
            </b-card-title>
          </b-card>
        </div>
        <div class="col-6">
          <b-card
            v-if="poidsPatient"
            :border-variant="dangerWeight ? 'danger' : ''"
            :header-bg-variant="dangerWeight ? 'danger' : ''"
            :header-text-variant="dangerWeight ? 'white' : ''"
            align="center"
            header="Poids (kg)"
          >
            <b-card-title>
              {{ poidsPatient[poidsPatient.length - 1]?.valeur || 'Aucune donnée' }}
            </b-card-title>

            <template #footer>
              <b-button v-b-modal.modal-poids variant="primary">Ajouter une valeur</b-button>
              <b-modal id="modal-poids" title="Ajouter une mesure de Poids" @ok="addPoidsValue">
                <b-form-input v-model="newWeightValue" placeholder="Valeur mesurée (kg)" type="number"></b-form-input>
              </b-modal>
              <b-button v-if="poidsPatient.length > 0" v-b-modal.modal-updatePoids class="ml-2" variant="outline-secondary">
                Modifier une ancienne valeur
              </b-button>
              <b-modal id="modal-updatePoids" v-model="showWeightModal" size="lg" title="Modifier des mesures de poids">
                <div class="row justify-content-between px-4 text-center h5">
                  <div class="col-5">Date</div>
                  <div class="col-5">Valeur (kg)</div>
                  <div class="col-2"></div>
                </div>
                <b-list-group flush>
                  <b-list-group-item v-for="(poids, index) in poidsPatient" :key="poids.id">
                    <div class="row justify-content-between">
                      <div class="col-5">
                        <b-form-input v-model="poids.date" type="datetime-local"></b-form-input>
                      </div>
                      <div class="col-5">
                        <b-form-input v-model="poids.valeur" type="number"></b-form-input>
                      </div>
                      <div class="col-2">
                        <b-button class="btn btn-sm" data-cy="entityDeleteButton" variant="danger" @click="removePoidsValue(index)">
                          <font-awesome-icon icon="times"></font-awesome-icon>
                          <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                        </b-button>
                      </div>
                    </div>
                  </b-list-group-item>
                </b-list-group>

                <template #modal-footer>
                  <div class="w-100">
                    <b-button class="float-right ml-2" variant="primary" @click="updatePoidsValues"> Enregistrer</b-button>
                    <b-button class="float-right" variant="secondary" @click="showWeightModal = false"> Fermer</b-button>
                  </div>
                </template>
              </b-modal>
            </template>
          </b-card>
        </div>
      </div>
      <div class="row justify-content-center mt-2">
        <div class="col-6">
          <b-card
            v-if="EPAPatient"
            :border-variant="dangerEPA ? 'danger' : ''"
            :header-bg-variant="dangerEPA ? 'danger' : ''"
            :header-text-variant="dangerEPA ? 'white' : ''"
            align="center"
            header="EPA"
            header="EPA"
          >
            <b-card-title>
              {{ EPAPatient[0]?.valeur || 'Aucune donnée' }}
            </b-card-title>

            <template #footer>
              <b-button v-b-modal.modal-epa variant="outline-primary">Ajouter une valeur</b-button>
              <b-modal id="modal-epa" title="Ajouter une mesure EPA" @ok="addEPAValue">
                <b-form-input v-model="newEPAValue" placeholder="Valeur mesurée" type="number"></b-form-input>
              </b-modal>
              <b-button v-if="EPAPatient.length > 0" v-b-modal.modal-updateEPA class="ml-2" variant="outline-secondary">
                Modifier une ancienne valeur
              </b-button>
              <b-modal id="modal-updateEPA" v-model="showEPAModal" size="lg" title="Modifier des mesures d'EPA">
                <div class="row justify-content-between px-4 text-center h5">
                  <div class="col-5">Date</div>
                  <div class="col-5">Valeur (kg)</div>
                  <div class="col-2"></div>
                </div>
                <b-list-group flush>
                  <b-list-group-item v-for="(epa, index) in EPAPatient" :key="epa.id">
                    <div class="row justify-content-between">
                      <div class="col-5">
                        <b-form-input v-model="epa.date" type="datetime-local"></b-form-input>
                      </div>
                      <div class="col-5">
                        <b-form-input v-model="epa.valeur" type="number"></b-form-input>
                      </div>
                      <div class="col-2">
                        <b-button class="btn btn-sm" data-cy="entityDeleteButton" variant="danger" @click="removeEPAValue(index)">
                          <font-awesome-icon icon="times"></font-awesome-icon>
                          <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                        </b-button>
                      </div>
                    </div>
                  </b-list-group-item>
                </b-list-group>

                <template #modal-footer>
                  <div class="w-100">
                    <b-button class="float-right ml-2" variant="primary" @click="updateEPAValues"> Enregistrer</b-button>
                    <b-button class="float-right" variant="secondary" @click="showEPAModal = false"> Fermer</b-button>
                  </div>
                </template>
              </b-modal>
            </template>
          </b-card>
        </div>
        <div class="col-6">
          <b-card v-if="albuPatient" align="center" header="Albumine (g/kg)">
            <b-card-title>
              {{ albuPatient[0]?.valeur || 'Aucune donnée' }}
            </b-card-title>

            <template #footer>
              <b-button v-b-modal.modal-albu variant="outline-primary">Ajouter une valeur</b-button>
              <b-modal id="modal-albu" title="Ajouter une mesure d'Albumine" @ok="addAlbuValue">
                <b-form-input v-model="newAlbuValue" placeholder="Valeur mesurée (g/kg)" type="number"></b-form-input>
              </b-modal>
              <b-button v-if="albuPatient.length > 0" v-b-modal.modal-updateAlbu class="ml-2" variant="outline-secondary">
                Modifier une ancienne valeur
              </b-button>
              <b-modal id="modal-updateAlbu" v-model="showAlbuModal" size="lg" title="Modifier des mesures d'Albumine">
                <div class="row justify-content-between px-4 text-center h5">
                  <div class="col-5">Date</div>
                  <div class="col-5">Valeur (kg)</div>
                  <div class="col-2"></div>
                </div>
                <b-list-group flush>
                  <b-list-group-item v-for="(albu, index) in albuPatient" :key="albu.id">
                    <div class="row justify-content-between">
                      <div class="col-5">
                        <b-form-input v-model="albu.date" type="datetime-local"></b-form-input>
                      </div>
                      <div class="col-5">
                        <b-form-input v-model="albu.valeur" type="number"></b-form-input>
                      </div>
                      <div class="col-2">
                        <b-button class="btn btn-sm" data-cy="entityDeleteButton" variant="danger" @click="removeAlbuValue(index)">
                          <font-awesome-icon icon="times"></font-awesome-icon>
                          <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                        </b-button>
                      </div>
                    </div>
                  </b-list-group-item>
                </b-list-group>

                <template #modal-footer>
                  <div class="w-100">
                    <b-button class="float-right ml-2" variant="primary" @click="updateAlbuValues"> Enregistrer</b-button>
                    <b-button class="float-right" variant="secondary" @click="showAlbuModal = false"> Fermer</b-button>
                  </div>
                </template>
              </b-modal>
            </template>
          </b-card>
        </div>
      </div>
    </div>
  </div>

  <div class="row mt-5">
    <div class="col-12">
      <h2>Repas</h2>
    </div>
    <div class="col-12">
      <b-table id="my-table" :current-page="tableCurrentPage" :items="patientMeals" :per-page="itemsPerPageTable" hover striped></b-table>
      <span v-if="patientMeals.length === 0"> Aucun repas ou apport enregistré </span>
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
          <h4 class="card-title" v-text="t$('ecom02App.patient.infoComplementaires')"></h4>
          <p class="card-text">
            {{ patient.infoComplementaires }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./patient-details.component.ts"></script>
