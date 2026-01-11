<script setup lang="ts">
  import { ref } from 'vue'
  import Login from '@/components/LoginForm.vue'
  import TabConsultation from "@/components/TabConsultation.vue";
  import ReservationConsultation from './components/ReservationConsultation.vue';

  const patientId = ref<string | null>(null)
  const refreshKey = ref(0)

  function onLoginSuccess(id: number) {
    patientId.value = id.toString()
  }

  function logout() {
    patientId.value = null
  }

  function deleteConsultation() {
    patientId.value = null
  }

  function refreshConsultations() {
    refreshKey.value++
  }

</script>

<template>
  <Login v-if="patientId === null" @login-success="onLoginSuccess" />

  <TabConsultation v-if="patientId != null"
      :patientId="patientId!"
      :key="refreshKey"
      @logout="logout"
      @newConsultation="() => {}"
      @deleteConsultation="deleteConsultation"
  />

  <ReservationConsultation v-if="patientId != null" :patient-id="patientId!" @reservation-done="refreshConsultations"/>
</template>