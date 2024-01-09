<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2EcomApp.medecin.home.createOrEditLabel"
          data-cy="MedecinCreateUpdateHeading"
          v-text="t$('g2EcomApp.medecin.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="medecin.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="medecin.id" readonly />
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.medecin.user')" for="medecin-user"></label>
            <select
              class="form-control"
              id="medecin-users"
              data-cy="user"
              multiple
              name="user"
              v-if="medecin.users !== undefined"
              v-model="medecin.users"
            >
              <option v-bind:value="getSelected(medecin.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('g2EcomApp.medecin.alerte')" for="medecin-alerte"></label>
            <select
              class="form-control"
              id="medecin-alertes"
              data-cy="alerte"
              multiple
              name="alerte"
              v-if="medecin.alertes !== undefined"
              v-model="medecin.alertes"
            >
              <option v-bind:value="getSelected(medecin.alertes, alerteOption)" v-for="alerteOption in alertes" :key="alerteOption.id">
                {{ alerteOption.id }}
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
<script lang="ts" src="./medecin-update.component.ts"></script>
