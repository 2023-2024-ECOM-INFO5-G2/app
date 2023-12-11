<template>
  <div class="home row">
    <div class="col-md-3">
      <h4 class="my-4">Liste d'établissements</h4>
      <b-list-group>
        <b-list-group-item class="d-flex justify-content-between align-items-center">
          Hopital Jeanne de Flandres
          <b-badge variant="danger" pill>14</b-badge>
        </b-list-group-item>

        <b-list-group-item class="d-flex justify-content-between align-items-center">
          Hopital Fleming
          <b-badge variant="danger" pill>2</b-badge>
        </b-list-group-item>

        <b-list-group-item class="d-flex justify-content-between align-items-center"> Hopital Pasteur </b-list-group-item>

        <b-list-group-item active class="d-flex justify-content-between align-items-center a"> Hopital Croix Rousse </b-list-group-item>

        <b-list-group-item class="d-flex justify-content-between align-items-center">
          CHU Grenoble Alpes
          <b-badge variant="danger" pill>1</b-badge>
        </b-list-group-item>
      </b-list-group>
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
