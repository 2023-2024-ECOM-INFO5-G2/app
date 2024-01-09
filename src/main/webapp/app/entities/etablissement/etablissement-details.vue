<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="etablissement">
        <h2 class="jh-entity-heading" data-cy="etablissementDetailsHeading">
          <span v-text="t$('ecom02App.etablissement.detail.title')"></span> {{ etablissement.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ecom02App.etablissement.nom')"></span>
          </dt>
          <dd>
            <span>{{ etablissement.nom }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.etablissement.adresse')"></span>
          </dt>
          <dd>
            <span>{{ etablissement.adresse }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.etablissement.ville')"></span>
          </dt>
          <dd>
            <span>{{ etablissement.ville }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.etablissement.codePostal')"></span>
          </dt>
          <dd>
            <span>{{ etablissement.codePostal }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.etablissement.user')"></span>
          </dt>
          <dd>
            <span v-for="(user, i) in etablissement.users" :key="user.id"
              >{{ i > 0 ? ', ' : '' }}
              {{ user.id }}
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="etablissement.id"
          :to="{ name: 'EtablissementEdit', params: { etablissementId: etablissement.id } }"
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

<script lang="ts" src="./etablissement-details.component.ts"></script>
