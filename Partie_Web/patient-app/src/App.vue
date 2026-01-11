<script setup lang="ts">
  import { ref } from 'vue'
  import Login from '@/components/LoginForm.vue'
  import TabConsultation from '@/components/TabConsultation.vue'
  import ReservationConsultation from '@/components/ReservationConsultation.vue'

  const patientId = ref<string | null>(null)
  const refreshKey = ref(0)
  const isReservation = ref(false)

  function onLoginSuccess(id: number) {
    patientId.value = id.toString()
  }

  function logout() {
    patientId.value = null
    isReservation.value = false
  }

  function refreshConsultations() {
    refreshKey.value++
    isReservation.value = false
  }

  function goToReservation() {
    isReservation.value = true
  }
</script>


<template>
  <Login
      v-if="patientId === null"
      @login-success="onLoginSuccess"
  />

  <TabConsultation
      v-else-if="!isReservation"
      :patientId="patientId!"
      :key="refreshKey"
      @logout="logout"
      @newConsultation="goToReservation"
  />

  <ReservationConsultation
      v-else
      :patient-id="patientId!"
      @reservation-done="refreshConsultations"
  />
</template>
