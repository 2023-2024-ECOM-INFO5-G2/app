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
              {{ patientIMC || t$('ecom02App.patient.noData') }}
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
              {{ poidsPatient[0]?.valeur || t$('ecom02App.patient.noData') }}
            </b-card-title>

            <template #footer>
              <b-button-group vertical>
                <b-button v-b-modal.modal-poids variant="primary">{{ t$('ecom02App.patient.addValue') }}</b-button>
                <b-button v-if="poidsPatient.length > 0" v-b-modal.modal-updatePoids class="mt-2" variant="outline-secondary">
                  {{ t$('ecom02App.patient.modify') }}
                </b-button>
              </b-button-group>

              <b-modal id="modal-poids" :title="t$('ecom02App.patient.addWeightMeasure')" @ok="addPoidsValue">
                <b-form-input v-model="newWeightValue" placeholder="Valeur mesurée (kg)" type="number"></b-form-input>
              </b-modal>

              <b-modal id="modal-updatePoids" v-model="showWeightModal" size="lg" :title="t$('ecom02App.patient.updateWeightMeasure')">
                <div class="row justify-content-between px-4 text-center h5">
                  <div class="col-lg-5 col-7">Date</div>
                  <div class="col-lg-5 col-3">Valeur (kg)</div>
                  <div class="col-2"></div>
                </div>
                <b-list-group flush>
                  <b-list-group-item v-for="(poids, index) in poidsPatient" :key="poids.id">
                    <div class="row justify-content-between">
                      <div class="col-lg-5 col-7">
                        <b-form-input v-model="poids.date" type="datetime-local"></b-form-input>
                      </div>
                      <div class="col-lg-5 col-3">
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
                    <b-button class="float-right ml-2" variant="primary" @click="updatePoidsValues">
                      {{ t$('ecom02App.patient.save') }}
                    </b-button>
                    <b-button class="float-right" variant="secondary" @click="showWeightModal = false">
                      {{ t$('ecom02App.patient.close') }}
                    </b-button>
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
          >
            <b-card-title>
              {{ EPAPatient[0]?.valeur || t$('ecom02App.patient.noData') }}
            </b-card-title>

            <template #footer>
              <b-button-group vertical>
                <b-button v-b-modal.modal-epa variant="outline-primary">{{ t$('ecom02App.patient.addValue') }}</b-button>
                <b-button v-if="EPAPatient.length > 0" v-b-modal.modal-updateEPA class="mt-2" variant="outline-secondary">
                  {{ t$('ecom02App.patient.modify') }}
                </b-button>
              </b-button-group>
              <b-modal id="modal-epa" :title="t$('ecom02App.patient.addEPAMeasure')" @ok="addEPAValue">
                <b-form-input v-model="newEPAValue" placeholder="Valeur mesurée" type="number"></b-form-input>
              </b-modal>
              <b-modal id="modal-updateEPA" v-model="showEPAModal" size="lg" :title="t$('ecom02App.patient.updateEPAMeasure')">
                <div class="row justify-content-between px-4 text-center h5">
                  <div class="col-lg-5 col-7">Date</div>
                  <div class="col-lg-5 col-3">Valeur (kg)</div>
                  <div class="col-2"></div>
                </div>
                <b-list-group flush>
                  <b-list-group-item v-for="(epa, index) in EPAPatient" :key="epa.id">
                    <div class="row justify-content-between">
                      <div class="col-7 col-lg-5">
                        <b-form-input v-model="epa.date" type="datetime-local"></b-form-input>
                      </div>
                      <div class="col-3 col-lg-5">
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
                    <b-button class="float-right ml-2" variant="primary" @click="updateEPAValues">
                      {{ t$('ecom02App.patient.save') }}
                    </b-button>
                    <b-button class="float-right" variant="secondary" @click="showEPAModal = false">
                      {{ t$('ecom02App.patient.close') }}</b-button
                    >
                  </div>
                </template>
              </b-modal>
            </template>
          </b-card>
        </div>
        <div class="col-6">
          <b-card v-if="albuPatient" align="center" header="Albumine (g/kg)">
            <b-card-title>
              {{ albuPatient[0]?.valeur || t$('ecom02App.patient.noData') }}
            </b-card-title>

            <template #footer>
              <b-button-group vertical>
                <b-button v-b-modal.modal-albu variant="outline-primary">{{ t$('ecom02App.patient.addValue') }}</b-button>
                <b-button v-if="albuPatient.length > 0" v-b-modal.modal-updateAlbu class="mt-2" variant="outline-secondary">
                  {{ t$('ecom02App.patient.modify') }}
                </b-button>
              </b-button-group>
              <b-modal id="modal-albu" :title="t$('ecom02App.patient.addAlbuMeasure')" @ok="addAlbuValue">
                <b-form-input v-model="newAlbuValue" placeholder="Valeur mesurée (g/kg)" type="number"></b-form-input>
              </b-modal>

              <b-modal id="modal-updateAlbu" v-model="showAlbuModal" size="lg" :title="t$('ecom02App.patient.updateAlbuMeasure')">
                <div class="row justify-content-between px-4 text-center h5">
                  <div class="col-lg-5 col-7">Date</div>
                  <div class="col-lg-5 col-3">Valeur (kg)</div>
                  <div class="col-2"></div>
                </div>
                <b-list-group flush>
                  <b-list-group-item v-for="(albu, index) in albuPatient" :key="albu.id">
                    <div class="row justify-content-between">
                      <div class="col-lg-5 col-7">
                        <b-form-input v-model="albu.date" type="datetime-local"></b-form-input>
                      </div>
                      <div class="col-lg-5 col-3">
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
                    <b-button class="float-right ml-2" variant="primary" @click="updateAlbuValues">
                      {{ t$('ecom02App.patient.save') }}
                    </b-button>
                    <b-button class="float-right" variant="secondary" @click="showAlbuModal = false">
                      {{ t$('ecom02App.patient.close') }}</b-button
                    >
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
      <span v-if="patientMeals.length === 0"> {{ t$('ecom02App.patient.noMeal') }}</span>
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
      <b-button v-b-modal.modal-repas variant="primary">{{ t$('ecom02App.patient.addMeal') }}</b-button>
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
