<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="ecom02App.mesurePoids.home.createOrEditLabel"
          data-cy="MesurePoidsCreateUpdateHeading"
          v-text="t$('ecom02App.mesurePoids.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mesurePoids.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mesurePoids.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.mesurePoids.valeur')" for="mesure-poids-valeur"></label>
            <input
              type="number"
              class="form-control"
              name="valeur"
              id="mesure-poids-valeur"
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
            <label class="form-control-label" v-text="t$('ecom02App.mesurePoids.date')" for="mesure-poids-date"></label>
            <div class="d-flex">
              <input
                id="mesure-poids-date"
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
            <label class="form-control-label" v-text="t$('ecom02App.mesurePoids.patient')" for="mesure-poids-patient"></label>
            <select class="form-control" id="mesure-poids-patient" data-cy="patient" name="patient" v-model="mesurePoids.patient">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="mesurePoids.patient && patientOption.id === mesurePoids.patient.id ? mesurePoids.patient : patientOption"
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
<script lang="ts" src="./mesure-poids-update.component.ts"></script>
