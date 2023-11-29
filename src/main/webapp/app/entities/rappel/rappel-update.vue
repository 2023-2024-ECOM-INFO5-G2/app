<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.rappel.home.createOrEditLabel"
          data-cy="RappelCreateUpdateHeading"
          v-text="t$('g2EcomApp.rappel.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="rappel.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="rappel.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.rappel.frequence')" for="rappel-frequence"></label>
            <input
              type="text"
              class="form-control"
              name="frequence"
              id="rappel-frequence"
              data-cy="frequence"
              :class="{ valid: !v$.frequence.$invalid, invalid: v$.frequence.$invalid }"
              v-model="v$.frequence.$model"
              required
            />
            <div v-if="v$.frequence.$anyDirty && v$.frequence.$invalid">
              <small class="form-text text-danger" v-for="error of v$.frequence.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.rappel.echeance')" for="rappel-echeance"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="rappel-echeance"
                  v-model="v$.echeance.$model"
                  name="echeance"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="rappel-echeance"
                data-cy="echeance"
                type="text"
                class="form-control"
                name="echeance"
                :class="{ valid: !v$.echeance.$invalid, invalid: v$.echeance.$invalid }"
                v-model="v$.echeance.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.echeance.$anyDirty && v$.echeance.$invalid">
              <small class="form-text text-danger" v-for="error of v$.echeance.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.rappel.tache')" for="rappel-tache"></label>
            <input
              type="text"
              class="form-control"
              name="tache"
              id="rappel-tache"
              data-cy="tache"
              :class="{ valid: !v$.tache.$invalid, invalid: v$.tache.$invalid }"
              v-model="v$.tache.$model"
              required
            />
            <div v-if="v$.tache.$anyDirty && v$.tache.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tache.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.rappel.patient')" for="rappel-patient"></label>
            <select class="form-control" id="rappel-patient" data-cy="patient" name="patient" v-model="rappel.patient">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="rappel.patient && patientOption.id === rappel.patient.id ? rappel.patient : patientOption"
                v-for="patientOption in patients"
                :key="patientOption.id"
              >
                {{ patientOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.rappel.medecin')" for="rappel-medecin"></label>
            <select
              class="form-control"
              id="rappel-medecins"
              data-cy="medecin"
              multiple
              name="medecin"
              v-if="rappel.medecins !== undefined"
              v-model="rappel.medecins"
            >
              <option v-bind:value="getSelected(rappel.medecins, medecinOption)" v-for="medecinOption in medecins" :key="medecinOption.id">
                {{ medecinOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./rappel-update.component.ts"></script>
