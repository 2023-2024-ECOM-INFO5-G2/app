<template>
  <div class="home row">
    <div class="col-md-3">
      <h4 class="card-header">Liste des établissements</h4>
      <ul class="list-group">
        <li
          class="list-group-item"
          v-for="etablissement in etablissements"
          :key="etablissement.id"
          @click="() => selectEtablissement(etablissement)"
        >
          <button>{{ etablissement.nom }}</button>
        </li>
      </ul>
    </div>

    <div class="col-md-9">
      <h1 class="display-4" v-text="t$('home.title')"></h1>

      <div>
        <div class="alert alert-success" v-if="authenticated">
          <span v-if="username" v-text="t$('home.logged.message', { username: username })"></span>
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          <span v-text="t$('global.messages.info.authenticated.prefix')"></span>
          <a class="alert-link" v-on:click="openLogin()" v-text="t$('global.messages.info.authenticated.link')"></a
          ><span v-html="t$('global.messages.info.authenticated.suffix')"></span>
        </div>
        <div class="alert alert-warning" v-if="!authenticated">
          <span v-text="t$('global.messages.info.register.noaccount')"></span>&nbsp;
          <router-link class="alert-link" to="/register" v-text="t$('global.messages.info.register.link')"></router-link>
        </div>
      </div>

      <div class="row justify-content-center text-center mt-5">
        <div class="col-4">
          <div class="card">
            <h6 class="card-header">{{ 'Etablissement séléctionné : Hopital Croix Rousse' }}</h6>
            <div class="card-body">
              <h5>{{ "informations sur l'etablissement choisi" }}</h5>
            </div>
          </div>
        </div>
        <div class="col-4">
          <div class="card">
            <h6 class="card-header">{{ 'Cas détectés' }}</h6>
            <div class="card-body">
              <h5>{{ '0' }}</h5>
            </div>
          </div>
        </div>
        <div class="col-4">
          <div class="card">
            <h6 class="card-header">{{ 'Staff' }}</h6>
            <div class="card-body">
              <h5>{{ '62 patients ; 10 AS ; 5 infirmières' }}</h5>
            </div>
          </div>
        </div>
      </div>

      <h1 class="my-4">Liste des patients</h1>

      <div>
        <b-table> </b-table>
      </div>

      <div class="row justify-content-end">
        <div class="col-md-4">
          <div class="card text-center">
            <h6 class="card-header">{{ 'Laisser une instruction' }}</h6>
            <div class="card-body">
              <a href="#" class="btn btn-primary">+</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
