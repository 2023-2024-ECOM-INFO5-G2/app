<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mesureAlbumine">
        <h2 class="jh-entity-heading" data-cy="mesureAlbumineDetailsHeading">
          <span v-text="t$('ecom02App.mesureAlbumine.detail.title')"></span> {{ mesureAlbumine.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ecom02App.mesureAlbumine.valeur')"></span>
          </dt>
          <dd>
            <span>{{ mesureAlbumine.valeur }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.mesureAlbumine.date')"></span>
          </dt>
          <dd>
            <span v-if="mesureAlbumine.date">{{ formatDateLong(mesureAlbumine.date) }}</span>
          </dd>
          <dt>
            <span v-text="t$('ecom02App.mesureAlbumine.patient')"></span>
          </dt>
          <dd>
            <div v-if="mesureAlbumine.patient">
              <router-link :to="{ name: 'PatientView', params: { patientId: mesureAlbumine.patient.id } }">{{
                mesureAlbumine.patient.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="mesureAlbumine.id"
          :to="{ name: 'MesureAlbumineEdit', params: { mesureAlbumineId: mesureAlbumine.id } }"
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

<script lang="ts" src="./mesure-albumine-details.component.ts"></script>
