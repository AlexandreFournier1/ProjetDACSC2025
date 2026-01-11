<script setup lang="ts">
    import { ref, onMounted } from 'vue'

    import type { Doctor } from '@/model/entity/Doctor'
    import type { Specialty } from '@/model/entity/Specialty'
    import type { Consultation } from '@/model/entity/Consultation'

    import { DoctorDAO_API } from '@/model/dao/DoctorDAO_API'
    import { SpecialtyDAO_API } from '@/model/dao/SpecialtyDAO_API'
    import { ConsultationDAO_API } from '@/model/dao/ConsultationDAO_API'
    import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'

    const props = defineProps<{patientId: string}>()
    const emit = defineEmits<{(e: 'reservation-done'): void}>()

    const doctors = ref<Doctor[]>([])
    const specialties = ref<Specialty[]>([])
    let consultations = ref<Consultation[]>([])

    const selectedId = ref<number | null>(null)

    const selectedDoctorName = ref("")
    const selectedSpecialtyName = ref("")

    const doctorDAO = new DoctorDAO_API()
    const specialtyDAO = new SpecialtyDAO_API()
    const consultationDAO = new ConsultationDAO_API()

    const showtab = ref(false)
    const showDialog = ref(false)
    const reason = ref("")
    const errorDialog = ref('')
    const errorSearch = ref('')

    async function loadDoctors() {
        doctors.value = await doctorDAO.load()
    }

    async function loadSpecialties() {
        specialties.value = await specialtyDAO.load()
    }

    async function loadConsultations() {
        errorSearch.value = "";
        showtab.value = false;

        const consultationVM: ConsultationVM = {
            doctor_name: selectedDoctorName.value,
            specialty: selectedSpecialtyName.value
        }

        consultations.value = await consultationDAO.search(consultationVM)

        if (consultations.value.length === 0) {
            errorSearch.value = "Pas de consultations correspondantes trouvées !"
            showtab.value = false
            return
        }

        showtab.value = true
    }

    async function reserveConsultation() {
        if (!selectedId.value) {
            return
        }

        errorDialog.value = ''
        reason.value = ''
        showDialog.value = true
    }

    async function confirmReservation() {
        if (reason.value.trim() === '') {
            errorDialog.value = 'La raison ne doit pas être vide'
            return
        }

        showDialog.value = false

        await consultationDAO.reserveConsultation({ id: selectedId.value!.toString(), reason: reason.value}, {id: props.patientId})

        loadConsultations()

        showtab.value = false;

        emit('reservation-done')
    }

    onMounted(() => {
        loadDoctors()
        loadSpecialties()

        console.log('DOCTORS', doctors.value)
        console.log('SPECIALTIES', specialties.value)
    })
</script>
<template>
    <div class="reservation-consultation">
        <div class="research-consultation">
          <button class="return" @click="emit('reservation-done')">Retour</button>
          <h3>Réservation : </h3>
            <section class="inner-section">
                <div class="label-column">
                    <label for="specialty">Spécialité :</label>
                </div>
                <div class="input-column">
                    <select id="specialty" v-model="selectedSpecialtyName">
                    <option :value="null">-- Toutes --</option>
                    <option v-for="s in specialties" :key="s.id" :value="s.name">
                        {{ s.name }}
                    </option>
                    </select>
                </div>
                </section>
                <section class="inner-section">
                <div class="label-column">
                    <label for="doctor">Médecin :</label>
                </div>
                <div class="input-column">
                    <select id="doctor" v-model="selectedDoctorName">
                    <option :value="null">-- Tous --</option>
                    <option v-for="d in doctors" :key="d.id" :value="d.lastName">
                        {{ d.lastName }}
                    </option>
                    </select>
                </div>
            </section>
            <button @click="loadConsultations()">Rechercher</button>
        </div>
        <section class="error-section" v-if="errorSearch">
            <p class="error-text">{{ errorSearch }}</p>
        </section>
        <div class="tab-consultation" v-if="showtab">
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
                    <tr v-for="consultation in consultations" :key="consultation.id" :class="{ selected: selectedId === consultation.id }" @click="selectedId = consultation.id">
                        <td>{{consultation.date}}</td>
                        <td>{{consultation.hour}}</td>
                        <td>{{consultation.doctor_name}}</td>
                        <td>{{consultation.specialty}}</td>
                        <td>{{consultation.reason}}</td>
                    </tr>
                </tbody>
            </table>
            <div class="button-section">
                <button :disabled="!selectedId" @click="reserveConsultation">
                    Réserver
                </button>
                <button class="cancel" @click="showtab = false" :disabled="!showtab">Annuler</button>
                <dialog v-if="showDialog" class="modal" open>
                    <h3>Motif de la consultation</h3>

                    <section class="error-section" v-if="errorDialog">
                        <p class="error-text">{{ errorDialog }}</p>
                    </section>

                    <section class="inner-section">
                        <div class="label-column">
                            <label for="reason">Raison :</label>
                        </div>
                        <div class="input-column">
                            <input v-model="reason" type="text" id="reason" placeholder="Ex : douleur thoracique">
                        </div>
                    </section>

                    <div class="modal-buttons">
                        <button @click="confirmReservation">Valider</button>
                        <button class="cancel" @click="showDialog = false" >Annuler</button>
                    </div>
                </dialog>
            </div>
        </div>
    </div>
</template>
<style>
    .reservation-consultation {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-family: Arial, Helvetica, sans-serif;
    }

    .research-consultation {
        display: flex;
        flex-direction: row;
    }

    .inner-section {
        padding: 19px;
        font-size: large;
        font-weight: 400;

        display: grid;
        grid-template-columns: repeat(2, 1fr);
        grid-template-rows: repeat(1, 1fr);
        gap: 4px;
    }

    .label-column {
        display: inline-block;
        text-align: start;
    }

    .input-column {
        display: inline-block;
        text-align: center;
    }

    .consultation {
        padding-top: 15vh;
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

    thead {
        background-color: green;
        color: white;
    }

    th, td {
        padding: 12px 16px;
        text-align: center;
    }

    tbody tr {
        cursor: pointer;
        transition: background-color 0.3s;
    }

    tbody tr:hover {
        background-color: #b6eeb6;
    }

    tbody tr.selected {
        background-color: #8fdc8f;
        font-weight: bold;
    }

    .consultation_table {
        border-collapse: collapse;
        background-color: lightgreen;
        border-radius: 10px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        overflow: hidden;
        margin-bottom: 20px;
    }

    button {
        display: inline-block;
        background-color: green;
        border-radius: 10px;
        border: 0px;
        text-align: center;
        padding: 10px;
        width: 125px;
        transition: all 0.5s;
        cursor: pointer;
        margin: 5px;
    }

    button:hover {
        background-color: lightgreen;
        border: 2px solid green;
    }

    .error-text {
        font-family: Arial, Helvetica, sans-serif;
        color: red;
        font-weight: bold;
    }

    .modal {
        border: none;
        border-radius: 12px;
        padding: 20px 25px;
        background-color: lightgreen;
        box-shadow: 0 4px 20px rgba(0,0,0,0.25);
        min-width: 350px;
    }

    .modal::backdrop {
        background: rgba(0, 0, 0, 0.4);
    }

    .modal h3 {
        margin-bottom: 15px;
        text-align: center;
        color: green;
    }

    .modal input {
        width: 100%;
        padding: 8px;
        border-radius: 6px;
        border: 1px solid #4caf50;
    }

    .modal-buttons {
        display: flex;
        justify-content: center;
        margin-top: 15px;
    }

    .modal-buttons .cancel {
        background-color: #aaa;
    }

    .modal-buttons .cancel:hover {
        background-color: #ddd;
        border-color: #888;
    }

</style>