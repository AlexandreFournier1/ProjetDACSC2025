<script setup lang="ts">
    import { ref, onMounted } from 'vue'

    import type { Doctor } from '@/model/entity/Doctor'
    import type { Specialty } from '@/model/entity/Specialty'
    import type { Consultation } from '@/model/entity/Consultation'

    import { DoctorDAO_API } from '@/model/dao/DoctorDAO_API'
    import { SpecialtyDAO_API } from '@/model/dao/SpecialtyDAO_API'

    const doctors = ref<Doctor[]>([])
    const specialties = ref<Specialty[]>([])
    const consultations = ref<Consultation[]>([])

    const selectedDoctorId = ref<number | null>(null)
    const selectedSpecialtyId = ref<number | null>(null)
    const selectedId = ref<number | null>(null)

    const doctorDAO = new DoctorDAO_API()
    const specialtyDAO = new SpecialtyDAO_API()

    let showtab = false;

    async function loadDoctors() {
        doctors.value = await doctorDAO.load()
    }

    async function loadSpecialties() {
        specialties.value = await specialtyDAO.load()
    }

    async function loadConsultations() {
        
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
            <h3>Réservation : </h3>
            <section class="inner-section">
                <div class="label-column">
                    <label for="specialty">Spécialité :</label>
                </div>
                <div class="input-column">
                    <select id="specialty" v-model="selectedSpecialtyId">
                    <option :value="null">-- Toutes --</option>
                    <option v-for="s in specialties" :key="s.id" :value="s.id">
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
                    <select id="doctor" v-model="selectedDoctorId">
                    <option :value="null">-- Tous --</option>
                    <option v-for="d in doctors" :key="d.id" :value="d.id">
                        {{ d.lastName }}
                    </option>
                    </select>
                </div>
            </section>
            <button>Rechercher</button>
        </div>
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
</style>