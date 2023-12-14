<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2ecomApp.mesureEPA.home.createOrEditLabel"
          data-cy="MesureEPACreateUpdateHeading"
          v-text="t$('g2ecomApp.mesureEPA.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mesureEPA.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mesureEPA.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.mesureEPA.valeur')" for="mesure-epa-valeur"></label>
            <input
              type="number"
              class="form-control"
              name="valeur"
              id="mesure-epa-valeur"
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
            <label class="form-control-label" v-text="t$('g2ecomApp.mesureEPA.date')" for="mesure-epa-date"></label>
            <div class="d-flex">
              <input
                id="mesure-epa-date"
                data-cy="date"
                type="datetime-local"
                class="form-control"
                name="date"
                :class="{ valid: !v$.date.$invalid, invalid: v$.date.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.date.$model)"
                @change="updateZonedDateTimeField('date', $event)"
              />
            </div>
            <div v-if="v$.date.$anyDirty && v$.date.$invalid">
              <small class="form-text text-danger" v-for="error of v$.date.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.mesureEPA.patient')" for="mesure-epa-patient"></label>
            <select class="form-control" id="mesure-epa-patient" data-cy="patient" name="patient" v-model="mesureEPA.patient">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="mesureEPA.patient && patientOption.id === mesureEPA.patient.id ? mesureEPA.patient : patientOption"
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
<script lang="ts" src="./mesure-epa-update.component.ts"></script>
