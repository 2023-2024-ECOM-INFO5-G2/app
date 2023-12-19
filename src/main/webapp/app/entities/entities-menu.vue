<template>
  <div>
    <b-dropdown-item to="/patient">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.patient')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/etablissement">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.etablissement')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/repas">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.repas')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/rappel">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.rappel')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/alerte">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.alerte')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/mesure-poids">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mesurePoids')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/mesure-epa">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mesureEpa')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/mesure-albumine">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mesureAlbumine')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
