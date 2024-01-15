<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="ecom02App.alerte.home.createOrEditLabel"
          data-cy="AlerteCreateUpdateHeading"
          v-text="t$('ecom02App.alerte.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="alerte.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="alerte.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.description')" for="alerte-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="alerte-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
              required
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.date')" for="alerte-date"></label>
            <div class="d-flex">
              <input
                id="alerte-date"
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
            <label class="form-control-label" v-text="t$('ecom02App.alerte.severe')" for="alerte-severe"></label>
            <input
              type="checkbox"
              class="form-check"
              name="severe"
              id="alerte-severe"
              data-cy="severe"
              :class="{ valid: !v$.severe.$invalid, invalid: v$.severe.$invalid }"
              v-model="v$.severe.$model"
              required
            />
            <div v-if="v$.severe.$anyDirty && v$.severe.$invalid">
              <small class="form-text text-danger" v-for="error of v$.severe.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.code')" for="alerte-code"></label>
            <input
              type="number"
              class="form-control"
              name="code"
              id="alerte-code"
              data-cy="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model.number="v$.code.$model"
              required
            />
            <div v-if="v$.code.$anyDirty && v$.code.$invalid">
              <small class="form-text text-danger" v-for="error of v$.code.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.patient')" for="alerte-patient"></label>
            <select class="form-control" id="alerte-patient" data-cy="patient" name="patient" v-model="alerte.patient">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="alerte.patient && patientOption.id === alerte.patient.id ? alerte.patient : patientOption"
                v-for="patientOption in patients"
                :key="patientOption.id"
              >
                {{ patientOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.mesureEPA')" for="alerte-mesureEPA"></label>
            <select class="form-control" id="alerte-mesureEPA" data-cy="mesureEPA" name="mesureEPA" v-model="alerte.mesureEPA">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="alerte.mesureEPA && mesureEPAOption.id === alerte.mesureEPA.id ? alerte.mesureEPA : mesureEPAOption"
                v-for="mesureEPAOption in mesureEPAS"
                :key="mesureEPAOption.id"
              >
                {{ mesureEPAOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.mesureAlbumine')" for="alerte-mesureAlbumine"></label>
            <select
              class="form-control"
              id="alerte-mesureAlbumine"
              data-cy="mesureAlbumine"
              name="mesureAlbumine"
              v-model="alerte.mesureAlbumine"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  alerte.mesureAlbumine && mesureAlbumineOption.id === alerte.mesureAlbumine.id
                    ? alerte.mesureAlbumine
                    : mesureAlbumineOption
                "
                v-for="mesureAlbumineOption in mesureAlbumines"
                :key="mesureAlbumineOption.id"
              >
                {{ mesureAlbumineOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.alerte.mesurePoids')" for="alerte-mesurePoids"></label>
            <select class="form-control" id="alerte-mesurePoids" data-cy="mesurePoids" name="mesurePoids" v-model="alerte.mesurePoids">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="alerte.mesurePoids && mesurePoidsOption.id === alerte.mesurePoids.id ? alerte.mesurePoids : mesurePoidsOption"
                v-for="mesurePoidsOption in mesurePoids"
                :key="mesurePoidsOption.id"
              >
                {{ mesurePoidsOption.id }}
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
