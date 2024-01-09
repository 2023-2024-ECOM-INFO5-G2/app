<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.aideSoignant.home.createOrEditLabel"
          data-cy="AideSoignantCreateUpdateHeading"
          v-text="t$('g2EcomApp.aideSoignant.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="aideSoignant.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="aideSoignant.id" readonly />
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.aideSoignant.user')" for="aide-soignant-user"></label>
            <select
              class="form-control"
              id="aide-soignant-users"
              data-cy="user"
              multiple
              name="user"
              v-if="aideSoignant.users !== undefined"
              v-model="aideSoignant.users"
            >
              <option v-bind:value="getSelected(aideSoignant.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.aideSoignant.patient')" for="aide-soignant-patient"></label>
            <select
              class="form-control"
              id="aide-soignant-patients"
              data-cy="patient"
              multiple
              name="patient"
              v-if="aideSoignant.patients !== undefined"
              v-model="aideSoignant.patients"
            >
              <option
                v-bind:value="getSelected(aideSoignant.patients, patientOption)"
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
<script lang="ts" src="./aide-soignant-update.component.ts"></script>
