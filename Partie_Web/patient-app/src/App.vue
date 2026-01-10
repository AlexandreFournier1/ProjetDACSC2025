<script setup lang="ts">
import { ref } from 'vue'
import Login from '@/components/LoginForm.vue'

const patientId = ref<number | null>(null)

function onLoginSuccess(id: number) {
  patientId.value = id
}

</script>

<template>
  <Login v-if="patientId === null" @login-success="onLoginSuccess" />
  <p v-else>
    Patient connecté (ID = {{ patientId }})
  </p>
</template>