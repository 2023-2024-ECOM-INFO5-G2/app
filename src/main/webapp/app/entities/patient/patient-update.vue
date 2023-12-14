<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="g2ecomApp.patient.home.createOrEditLabel"
          data-cy="PatientCreateUpdateHeading"
          v-text="t$('g2ecomApp.patient.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="patient.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="patient.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.prenom')" for="patient-prenom"></label>
            <input
              type="text"
              class="form-control"
              name="prenom"
              id="patient-prenom"
              data-cy="prenom"
              :class="{ valid: !v$.prenom.$invalid, invalid: v$.prenom.$invalid }"
              v-model="v$.prenom.$model"
              required
            />
            <div v-if="v$.prenom.$anyDirty && v$.prenom.$invalid">
              <small class="form-text text-danger" v-for="error of v$.prenom.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.nom')" for="patient-nom"></label>
            <input
              type="text"
              class="form-control"
              name="nom"
              id="patient-nom"
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
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.sexe')" for="patient-sexe"></label>
            <input
              type="text"
              class="form-control"
              name="sexe"
              id="patient-sexe"
              data-cy="sexe"
              :class="{ valid: !v$.sexe.$invalid, invalid: v$.sexe.$invalid }"
              v-model="v$.sexe.$model"
              required
            />
            <div v-if="v$.sexe.$anyDirty && v$.sexe.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sexe.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.taille')" for="patient-taille"></label>
            <input
              type="number"
              class="form-control"
              name="taille"
              id="patient-taille"
              data-cy="taille"
              :class="{ valid: !v$.taille.$invalid, invalid: v$.taille.$invalid }"
              v-model.number="v$.taille.$model"
              required
            />
            <div v-if="v$.taille.$anyDirty && v$.taille.$invalid">
              <small class="form-text text-danger" v-for="error of v$.taille.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.dateDeNaissance')" for="patient-dateDeNaissance"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="patient-dateDeNaissance"
                  v-model="v$.dateDeNaissance.$model"
                  name="dateDeNaissance"
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
                id="patient-dateDeNaissance"
                data-cy="dateDeNaissance"
                type="text"
                class="form-control"
                name="dateDeNaissance"
                :class="{ valid: !v$.dateDeNaissance.$invalid, invalid: v$.dateDeNaissance.$invalid }"
                v-model="v$.dateDeNaissance.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.dateDeNaissance.$anyDirty && v$.dateDeNaissance.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dateDeNaissance.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.numChambre')" for="patient-numChambre"></label>
            <input
              type="number"
              class="form-control"
              name="numChambre"
              id="patient-numChambre"
              data-cy="numChambre"
              :class="{ valid: !v$.numChambre.$invalid, invalid: v$.numChambre.$invalid }"
              v-model.number="v$.numChambre.$model"
              required
            />
            <div v-if="v$.numChambre.$anyDirty && v$.numChambre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numChambre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.taille')" for="patient-taille"></label>
            <input
              type="number"
              class="form-control"
              name="taille"
              id="patient-taille"
              data-cy="taille"
              :class="{ valid: !v$.taille.$invalid, invalid: v$.taille.$invalid }"
              v-model.number="v$.taille.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.dateArrivee')" for="patient-dateArrivee"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="patient-dateArrivee"
                  v-model="v$.dateArrivee.$model"
                  name="dateArrivee"
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
                id="patient-dateArrivee"
                data-cy="dateArrivee"
                type="datetime-local"
                class="form-control"
                name="dateArrivee"
                :class="{ valid: !v$.dateArrivee.$invalid, invalid: v$.dateArrivee.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.dateArrivee.$model)"
                @change="updateZonedDateTimeField('dateArrivee', $event)"
              />
            </b-input-group>
            <div v-if="v$.dateArrivee.$anyDirty && v$.dateArrivee.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dateArrivee.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('g2ecomApp.patient.infosComplementaires')"
              for="patient-infosComplementaires"
            ></label>
            <textarea
              class="form-control"
              name="infosComplementaires"
              id="patient-infosComplementaires"
              data-cy="infosComplementaires"
              :class="{ valid: !v$.infosComplementaires.$invalid, invalid: v$.infosComplementaires.$invalid }"
              v-model="v$.infosComplementaires.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label v-text="t$('g2ecomApp.patient.user')" for="patient-user"></label>
            <select
              class="form-control"
              id="patient-users"
              data-cy="user"
              multiple
              name="user"
              v-if="patient.users !== undefined"
              v-model="patient.users"
            >
              <option v-bind:value="getSelected(patient.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('g2ecomApp.patient.etablissement')" for="patient-etablissement"></label>
            <select
              class="form-control"
              id="patient-etablissement"
              data-cy="etablissement"
              name="etablissement"
              v-model="patient.etablissement"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  patient.etablissement && etablissementOption.id === patient.etablissement.id ? patient.etablissement : etablissementOption
                "
                v-for="etablissementOption in etablissements"
                :key="etablissementOption.id"
              >
                {{ etablissementOption.id }}
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
<script lang="ts" src="./patient-update.component.ts"></script>
