<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="repas">
        <h2 class="jh-entity-heading" data-cy="repasDetailsHeading">
          <span v-text="t$('g2ecomApp.repas.detail.title')"></span> {{ repas.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('g2ecomApp.repas.nom')"></span>
          </dt>
          <dd>
            <span>{{ repas.nom }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2ecomApp.repas.date')"></span>
          </dt>
          <dd>
            <span v-if="repas.date">{{ formatDateLong(repas.date) }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2ecomApp.repas.apportCalorique')"></span>
          </dt>
          <dd>
            <span>{{ repas.apportCalorique }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2ecomApp.repas.poidsConsomme')"></span>
          </dt>
          <dd>
            <span>{{ repas.poidsConsomme }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2ecomApp.repas.description')"></span>
          </dt>
          <dd>
            <span>{{ repas.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2ecomApp.repas.patient')"></span>
          </dt>
          <dd>
            <div v-if="repas.patient">
              <router-link :to="{ name: 'PatientView', params: { patientId: repas.patient.id } }">{{ repas.patient.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="repas.id" :to="{ name: 'RepasEdit', params: { repasId: repas.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./repas-details.component.ts"></script>
