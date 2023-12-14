<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2ecomApp.rappel.home.createOrEditLabel"
          data-cy="RappelCreateUpdateHeading"
          v-text="t$('g2ecomApp.rappel.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="rappel.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="rappel.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.rappel.date')" for="rappel-date"></label>
            <div class="d-flex">
              <input
                id="rappel-date"
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
            <label class="form-control-label" v-text="t$('g2ecomApp.rappel.frequenceJour')" for="rappel-frequenceJour"></label>
            <input
              type="number"
              class="form-control"
              name="frequenceJour"
              id="rappel-frequenceJour"
              data-cy="frequenceJour"
              :class="{ valid: !v$.frequenceJour.$invalid, invalid: v$.frequenceJour.$invalid }"
              v-model.number="v$.frequenceJour.$model"
              required
            />
            <div v-if="v$.frequenceJour.$anyDirty && v$.frequenceJour.$invalid">
              <small class="form-text text-danger" v-for="error of v$.frequenceJour.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.rappel.echeance')" for="rappel-echeance"></label>
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
            <label class="form-control-label" v-text="t$('g2ecomApp.rappel.tache')" for="rappel-tache"></label>
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
            <label v-text="t$('g2ecomApp.rappel.user')" for="rappel-user"></label>
            <select
              class="form-control"
              id="rappel-users"
              data-cy="user"
              multiple
              name="user"
              v-if="rappel.users !== undefined"
              v-model="rappel.users"
            >
              <option v-bind:value="getSelected(rappel.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.rappel.patient')" for="rappel-patient"></label>
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
