<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="ecom02App.etablissement.home.createOrEditLabel"
          data-cy="EtablissementCreateUpdateHeading"
          v-text="t$('ecom02App.etablissement.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="etablissement.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="etablissement.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.etablissement.nom')" for="etablissement-nom"></label>
            <input
              type="text"
              class="form-control"
              name="nom"
              id="etablissement-nom"
              data-cy="nom"
              :class="{ valid: !v$.nom.$invalid, invalid: v$.nom.$invalid }"
              v-model="v$.nom.$model"
            />
            <div v-if="v$.nom.$anyDirty && v$.nom.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nom.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.etablissement.adresse')" for="etablissement-adresse"></label>
            <input
              type="text"
              class="form-control"
              name="adresse"
              id="etablissement-adresse"
              data-cy="adresse"
              :class="{ valid: !v$.adresse.$invalid, invalid: v$.adresse.$invalid }"
              v-model="v$.adresse.$model"
              required
            />
            <div v-if="v$.adresse.$anyDirty && v$.adresse.$invalid">
              <small class="form-text text-danger" v-for="error of v$.adresse.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.etablissement.ville')" for="etablissement-ville"></label>
            <input
              type="text"
              class="form-control"
              name="ville"
              id="etablissement-ville"
              data-cy="ville"
              :class="{ valid: !v$.ville.$invalid, invalid: v$.ville.$invalid }"
              v-model="v$.ville.$model"
              required
            />
            <div v-if="v$.ville.$anyDirty && v$.ville.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ville.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ecom02App.etablissement.codePostal')" for="etablissement-codePostal"></label>
            <input
              type="text"
              class="form-control"
              name="codePostal"
              id="etablissement-codePostal"
              data-cy="codePostal"
              :class="{ valid: !v$.codePostal.$invalid, invalid: v$.codePostal.$invalid }"
              v-model="v$.codePostal.$model"
              required
            />
            <div v-if="v$.codePostal.$anyDirty && v$.codePostal.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codePostal.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('ecom02App.etablissement.user')" for="etablissement-user"></label>
            <select
              class="form-control"
              id="etablissement-users"
              data-cy="user"
              multiple
              name="user"
              v-if="etablissement.users !== undefined"
              v-model="etablissement.users"
            >
              <option v-bind:value="getSelected(etablissement.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.id }}
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
<script lang="ts" src="./etablissement-update.component.ts"></script>
