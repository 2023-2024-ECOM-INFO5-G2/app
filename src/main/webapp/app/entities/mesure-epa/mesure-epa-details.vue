<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mesureEPA">
        <h2 class="jh-entity-heading" data-cy="mesureEPADetailsHeading">
          <span v-text="t$('ecom02App.mesureEPA.detail.title')"></span> {{ mesureEPA.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ecom02App.mesureEPA.valeur')"></span>
          </dt>
          <dd>
            <span>{{ mesureEPA.valeur }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.mesureEPA.date')"></span>
          </dt>
          <dd>
            <span v-if="mesureEPA.date">{{ formatDateLong(mesureEPA.date) }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.mesureEPA.patient')"></span>
          </dt>
          <dd>
            <div v-if="mesureEPA.patient">
              <router-link :to="{ name: 'PatientView', params: { patientId: mesureEPA.patient.id } }">{{
                mesureEPA.patient.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="mesureEPA.id"
          :to="{ name: 'MesureEPAEdit', params: { mesureEPAId: mesureEPA.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./mesure-epa-details.component.ts"></script>
