<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.repas.home.createOrEditLabel"
          data-cy="RepasCreateUpdateHeading"
          v-text="t$('g2EcomApp.repas.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="repas.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="repas.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.repas.nom')" for="repas-nom"></label>
            <input
              type="text"
              class="form-control"
              name="nom"
              id="repas-nom"
              data-cy="nom"
              :class="{ valid: !v$.nom.$invalid, invalid: v$.nom.$invalid }"
              v-model="v$.nom.$model"
              required
            />
            <div v-if="v$.nom.$anyDirty && v$.nom.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nom.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2EcomApp.repas.description')" for="repas-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="repas-description"
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
            <label class="form-control-label" v-text="t$('g2EcomApp.repas.apportCalorique')" for="repas-apportCalorique"></label>
            <input
              type="number"
              class="form-control"
              name="apportCalorique"
              id="repas-apportCalorique"
              data-cy="apportCalorique"
              :class="{ valid: !v$.apportCalorique.$invalid, invalid: v$.apportCalorique.$invalid }"
              v-model.number="v$.apportCalorique.$model"
              required
            />
            <div v-if="v$.apportCalorique.$anyDirty && v$.apportCalorique.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apportCalorique.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.repas.patient')" for="repas-patient"></label>
            <select
              class="form-control"
              id="repas-patients"
              data-cy="patient"
              multiple
              name="patient"
              v-if="repas.patients !== undefined"
              v-model="repas.patients"
            >
              <option v-bind:value="getSelected(repas.patients, patientOption)" v-for="patientOption in patients" :key="patientOption.id">
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
<script lang="ts" src="./repas-update.component.ts"></script>
