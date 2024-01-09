<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mesure">
        <h2 class="jh-entity-heading" data-cy="mesureDetailsHeading">
          <span v-text="t$('g2EcomApp.mesure.detail.title')"></span> {{ mesure.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('g2EcomApp.mesure.date')"></span>
          </dt>
          <dd>
            <span>{{ mesure.date }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2EcomApp.mesure.nomValeur')"></span>
          </dt>
          <dd>
            <span>{{ mesure.nomValeur }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2EcomApp.mesure.valeur')"></span>
          </dt>
          <dd>
            <span>{{ mesure.valeur }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2EcomApp.mesure.patient')"></span>
          </dt>
          <dd>
            <div v-if="mesure.patient">
              <router-link :to="{ name: 'PatientView', params: { patientId: mesure.patient.id } }">{{ mesure.patient.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="mesure.id" :to="{ name: 'MesureEdit', params: { mesureId: mesure.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./mesure-details.component.ts"></script>
