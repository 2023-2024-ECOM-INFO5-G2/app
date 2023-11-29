<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.alerte.home.createOrEditLabel"
          data-cy="AlerteCreateUpdateHeading"
          v-text="t$('g2EcomApp.alerte.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="alerte.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="alerte.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.alerte.description')" for="alerte-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="alerte-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.alerte.date')" for="alerte-date"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="alerte-date"
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
                id="alerte-date"
                data-cy="date"
                type="text"
                class="form-control"
                name="date"
                :class="{ valid: !v$.date.$invalid, invalid: v$.date.$invalid }"
                v-model="v$.date.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.alerte.patient')" for="alerte-patient"></label>
            <select
              class="form-control"
              id="alerte-patients"
              data-cy="patient"
              multiple
              name="patient"
              v-if="alerte.patients !== undefined"
              v-model="alerte.patients"
            >
              <option v-bind:value="getSelected(alerte.patients, patientOption)" v-for="patientOption in patients" :key="patientOption.id">
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
<script lang="ts" src="./alerte-update.component.ts"></script>
