<script setup >
  import {ref} from 'vue'
  const consultations = ref([
    {
      id: 1,
      date: '2026-01-10',
      hour: '09:00',
      doctor: 'Dr Dupont',
      specialty: 'Cardiologie',
      reason: 'Contrôle annuel'
    },
    {
      id: 2,
      date: '2026-01-12',
      hour: '14:30',
      doctor: 'Dr Martin',
      specialty: 'Dermatologie',
      reason: 'Éruption cutanée'
    }
  ])

</script>

<template>
  <div class="consultation">
    <h3> Vos Consultations réservées</h3>

    <table class="consultation_table">
      <thead>
        <tr>
          <th>Date</th>
          <th>Heure</th>
          <th>Médecin</th>
          <th>Spécialité</th>
          <th>Raison</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="consultation in consultations":key="consultation.id"
            @click="selectedId = consultation.id">
          <td>{{consultation.date}}</td>
          <td>{{consultation.hour}}</td>
          <td>{{consultation.doctor}}</td>
          <td>{{consultation.specialty}}</td>
          <td>{{consultation.reason}}</td>
        </tr>
      </tbody>
    </table>

    <div>
      <button @click="$emit('logout')">
        Logout
      </button>

      <button @click="$emit('deleteConsultation')">
        Supprimer
      </button>

      <button @click="$emit('newConsultation')">
        Prendre RDV
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>