<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="rappel">
        <h2 class="jh-entity-heading" data-cy="rappelDetailsHeading">
          <span v-text="t$('ecom02App.rappel.detail.title')"></span> {{ rappel.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ecom02App.rappel.date')"></span>
          </dt>
          <dd>
            <span v-if="rappel.date">{{ formatDateLong(rappel.date) }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.rappel.echeance')"></span>
          </dt>
          <dd>
            <span v-if="rappel.echeance">{{ formatDateLong(rappel.echeance) }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.rappel.intervaleJours')"></span>
          </dt>
          <dd>
            <span>{{ rappel.intervaleJours }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.rappel.tache')"></span>
          </dt>
          <dd>
            <span>{{ rappel.tache }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.rappel.feeDansLetang')"></span>
          </dt>
          <dd>
            <span>{{ rappel.feeDansLetang }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.rappel.user')"></span>
          </dt>
          <dd>
            <span v-for="(user, i) in rappel.users" :key="user.id"
              >{{ i > 0 ? ', ' : '' }}
              {{ user.id }}
            </span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.rappel.patient')"></span>
          </dt>
          <dd>
            <div v-if="rappel.patient">
              <router-link :to="{ name: 'PatientView', params: { patientId: rappel.patient.id } }">{{ rappel.patient.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="rappel.id" :to="{ name: 'RappelEdit', params: { rappelId: rappel.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./rappel-details.component.ts"></script>
