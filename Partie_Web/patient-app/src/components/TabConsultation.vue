<script setup lang="ts">
import { ref, watch } from 'vue'
import { ConsultationDAO_API } from '@/model/dao/ConsultationDAO_API'
import type { Consultation } from '@/model/entity/Consultation'
import type { PatientVM } from '@/model/viewmodel/PatientVM'
import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'

// recup l'idPatient
const props = defineProps<{
  patientId: string
}>()
console.log('Patient ID reçu :', props.patientId)

const emit = defineEmits<{ (e: 'logout'): void, (e: 'newConsultation'): void, (e: 'deleteSelectedConsultation'): void }>()

let consultations = ref<Consultation[]>([])
const selectedId = ref<number | null>(null)
const error = ref('')

const dao = new ConsultationDAO_API()

async function loadConsultations() {
  try {
    console.log("load test1")
    const patientVM: PatientVM = {id: props.patientId}

    consultations.value = await dao.getConsultationsByPatientId(patientVM)

  } catch (e: any) {
    error.value = e.message || 'Erreur lors du chargement des consultations'
  }
}
async function deleteSelectedConsultation() {
  if (!selectedId.value) {
    return
  }

  if (!confirm('Voulez-vous vraiment supprimer ce rendez-vous ?')) {
    return
  }

  try {
    const consultationVM: ConsultationVM = {id: selectedId.value.toString()}

    await dao.deleteConsultation(consultationVM)

    await loadConsultations()
    selectedId.value = null
  } catch (e: any) {
    error.value = e.message || 'Erreur lors de la suppression'
  }
}

watch(
    () => props.patientId,
    (newId) => {
      if (newId) {
        console.log("load test2")
        loadConsultations()
      }
    },
    { immediate: true }
)


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
      <tr v-for="consultation in consultations"
          :key="consultation.id"
          :class="{ selected: selectedId === consultation.id }"
          @click="selectedId = consultation.id">

        <td>{{consultation.date}}</td>
        <td>{{consultation.hour}}</td>
        <td>{{consultation.doctor_name}}</td>
        <td>{{consultation.specialty}}</td>
        <td>{{consultation.reason}}</td>
      </tr>
      </tbody>
    </table>

    <div>
      <button @click="$emit('logout')">
        Logout
      </button>

      <button @click="deleteSelectedConsultation" :disabled="!selectedId">
        Supprimer
      </button>

      <button @click="$emit('newConsultation')">
        Prendre RDV
      </button>
    </div>
  </div>
</template>

<style scoped>
.consultation {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-family: Arial, Helvetica, sans-serif;
}

.consultation h3 {
  margin-bottom: 15px;
  text-align: center;
}

.consultation_table {
  border-collapse: collapse;
  background-color: lightgreen;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  overflow: hidden;
  margin-bottom: 20px;
}

.consultation_table thead {
  background-color: green;
  color: white;
}

.consultation_table th,
.consultation_table td {
  padding: 12px 16px;
  text-align: center;
}

.consultation_table tbody tr {
  cursor: pointer;
  transition: background-color 0.3s;
}

.consultation_table tbody tr:hover {
  background-color: #b6eeb6;
}

.consultation_table tbody tr.selected {
  background-color: #8fdc8f;
  font-weight: bold;
}

.consultation_buttons {
  display: flex;
  gap: 15px;
}

.consultation_buttons button {
  background-color: green;
  border-radius: 10px;
  border: 0;
  padding: 10px 15px;
  width: 180px;
  cursor: pointer;
  transition: all 0.5s;
}

.consultation_buttons button:hover {
  background-color: lightgreen;
  border: 2px solid green;
}

</style>