<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="medecin">
        <h2 class="jh-entity-heading" data-cy="medecinDetailsHeading">
          <span v-text="t$('g2EcomApp.medecin.detail.title')"></span> {{ medecin.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('g2EcomApp.medecin.user')"></span>
          </dt>
          <dd>
            <span v-for="(user, i) in medecin.users" :key="user.id"
              >{{ i > 0 ? ', ' : '' }}
              {{ user.id }}
            </span>
          </dd>
          <dt>
            <span v-text="t$('g2EcomApp.medecin.alerte')"></span>
          </dt>
          <dd>
            <span v-for="(alerte, i) in medecin.alertes" :key="alerte.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'AlerteView', params: { alerteId: alerte.id } }">{{ alerte.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="medecin.id" :to="{ name: 'MedecinEdit', params: { medecinId: medecin.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./medecin-details.component.ts"></script>
