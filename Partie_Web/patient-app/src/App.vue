<script setup lang="ts">
  import { ref } from 'vue'
  import Login from '@/components/LoginForm.vue'
  import TabConsultation from "@/components/TabConsultation.vue";
  import ReservationConsultation from './components/ReservationConsultation.vue';
  const patientId = ref<string | null>(null)

  function onLoginSuccess(id: number) {
    patientId.value = id.toString()
  }

  function logout() {
    patientId.value = null
  }

  function deleteConsultation() {
    patientId.value = null
  }

</script>

<template>
  <Login v-if="patientId === null" @login-success="onLoginSuccess" />

  <TabConsultation v-if="patientId != null"
      :patientId="patientId!"
      @logout="logout"
      @newConsultation="() => {}"
      @deleteConsultation="deleteConsultation"
  />

  <ReservationConsultation v-if="patientId != null"/>
</template>