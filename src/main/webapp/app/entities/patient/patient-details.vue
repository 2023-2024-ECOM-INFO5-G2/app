<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="patient">
        <h2 class="jh-entity-heading" data-cy="patientDetailsHeading">
          <span v-text="t$('ecom02App.patient.detail.title')"></span> {{ patient.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ecom02App.patient.prenom')"></span>
          </dt>
          <dd>
            <span>{{ patient.prenom }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.nom')"></span>
          </dt>
          <dd>
            <span>{{ patient.nom }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.sexe')"></span>
          </dt>
          <dd>
            <span>{{ patient.sexe }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.taille')"></span>
          </dt>
          <dd>
            <span>{{ patient.taille }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.dateDeNaissance')"></span>
          </dt>
          <dd>
            <span>{{ patient.dateDeNaissance }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.numChambre')"></span>
          </dt>
          <dd>
            <span>{{ patient.numChambre }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.dateArrivee')"></span>
          </dt>
          <dd>
            <span v-if="patient.dateArrivee">{{ formatDateLong(patient.dateArrivee) }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.infoComplementaires')"></span>
          </dt>
          <dd>
            <span>{{ patient.infoComplementaires }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.user')"></span>
          </dt>
          <dd>
            <span v-for="(user, i) in patient.users" :key="user.id"
              >{{ i > 0 ? ', ' : '' }}
              {{ user.id }}
            </span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.patient.etablissement')"></span>
          </dt>
          <dd>
            <div v-if="patient.etablissement">
              <router-link :to="{ name: 'EtablissementView', params: { etablissementId: patient.etablissement.id } }">{{
                patient.etablissement.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="patient.id" :to="{ name: 'PatientEdit', params: { patientId: patient.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./patient-details.component.ts"></script>
