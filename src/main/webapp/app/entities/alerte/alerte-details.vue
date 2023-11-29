<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="alerte">
        <h2 class="jh-entity-heading" data-cy="alerteDetailsHeading">
          <span v-text="t$('g2EcomApp.alerte.detail.title')"></span> {{ alerte.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('g2EcomApp.alerte.description')"></span>
          </dt>
          <dd>
            <span>{{ alerte.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2EcomApp.alerte.date')"></span>
          </dt>
          <dd>
            <span>{{ alerte.date }}</span>
          </dd>
          <dt>
            <span v-text="t$('g2EcomApp.alerte.patient')"></span>
          </dt>
          <dd>
            <span v-for="(patient, i) in alerte.patients" :key="patient.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'PatientView', params: { patientId: patient.id } }">{{ patient.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="alerte.id" :to="{ name: 'AlerteEdit', params: { alerteId: alerte.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alerte-details.component.ts"></script>
