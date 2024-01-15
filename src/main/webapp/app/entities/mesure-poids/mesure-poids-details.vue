<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mesurePoids">
        <h2 class="jh-entity-heading" data-cy="mesurePoidsDetailsHeading">
          <span v-text="t$('ecom02App.mesurePoids.detail.title')"></span> {{ mesurePoids.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ecom02App.mesurePoids.valeur')"></span>
          </dt>
          <dd>
            <span>{{ mesurePoids.valeur }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.mesurePoids.date')"></span>
          </dt>
          <dd>
            <span v-if="mesurePoids.date">{{ formatDateLong(mesurePoids.date) }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.mesurePoids.patient')"></span>
          </dt>
          <dd>
            <div v-if="mesurePoids.patient">
              <router-link :to="{ name: 'PatientView', params: { patientId: mesurePoids.patient.id } }">{{
                mesurePoids.patient.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="mesurePoids.id"
          :to="{ name: 'MesurePoidsEdit', params: { mesurePoidsId: mesurePoids.id } }"
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

<script lang="ts" src="./mesure-poids-details.component.ts"></script>
