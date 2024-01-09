<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.mesure.home.createOrEditLabel"
          data-cy="MesureCreateUpdateHeading"
          v-text="t$('g2EcomApp.mesure.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mesure.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mesure.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.mesure.date')" for="mesure-date"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="mesure-date"
                  v-model="v$.date.$model"
                  name="date"
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
                id="mesure-date"
                data-cy="date"
                type="text"
                class="form-control"
                name="date"
                :class="{ valid: !v$.date.$invalid, invalid: v$.date.$invalid }"
                v-model="v$.date.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.date.$anyDirty && v$.date.$invalid">
              <small class="form-text text-danger" v-for="error of v$.date.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.mesure.nomValeur')" for="mesure-nomValeur"></label>
            <input
              type="text"
              class="form-control"
              name="nomValeur"
              id="mesure-nomValeur"
              data-cy="nomValeur"
              :class="{ valid: !v$.nomValeur.$invalid, invalid: v$.nomValeur.$invalid }"
              v-model="v$.nomValeur.$model"
              required
            />
            <div v-if="v$.nomValeur.$anyDirty && v$.nomValeur.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nomValeur.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.mesure.valeur')" for="mesure-valeur"></label>
            <input
              type="number"
              class="form-control"
              name="valeur"
              id="mesure-valeur"
              data-cy="valeur"
              :class="{ valid: !v$.valeur.$invalid, invalid: v$.valeur.$invalid }"
              v-model.number="v$.valeur.$model"
              required
            />
            <div v-if="v$.valeur.$anyDirty && v$.valeur.$invalid">
              <small class="form-text text-danger" v-for="error of v$.valeur.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.mesure.patient')" for="mesure-patient"></label>
            <select class="form-control" id="mesure-patient" data-cy="patient" name="patient" v-model="mesure.patient">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="mesure.patient && patientOption.id === mesure.patient.id ? mesure.patient : patientOption"
                v-for="patientOption in patients"
                :key="patientOption.id"
              >
                {{ patientOption.id }}
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
<script lang="ts" src="./mesure-update.component.ts"></script>
