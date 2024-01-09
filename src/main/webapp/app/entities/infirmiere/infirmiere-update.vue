<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.infirmiere.home.createOrEditLabel"
          data-cy="InfirmiereCreateUpdateHeading"
          v-text="t$('g2EcomApp.infirmiere.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="infirmiere.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="infirmiere.id" readonly />
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.infirmiere.user')" for="infirmiere-user"></label>
            <select
              class="form-control"
              id="infirmiere-users"
              data-cy="user"
              multiple
              name="user"
              v-if="infirmiere.users !== undefined"
              v-model="infirmiere.users"
            >
              <option v-bind:value="getSelected(infirmiere.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.infirmiere.patient')" for="infirmiere-patient"></label>
            <select
              class="form-control"
              id="infirmiere-patients"
              data-cy="patient"
              multiple
              name="patient"
              v-if="infirmiere.patients !== undefined"
              v-model="infirmiere.patients"
            >
              <option
                v-bind:value="getSelected(infirmiere.patients, patientOption)"
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
<script lang="ts" src="./infirmiere-update.component.ts"></script>
