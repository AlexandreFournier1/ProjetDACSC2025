<script lang="ts">
    import { ref } from 'vue';

    const specialty = ref('');
    const medecin = ref('');
</script>
<template>
    <div class="research-consultation">
        <section class="inner-section">
            <div class="label-column">
                <label for="specialty">Spécialité :</label>
            </div>
            <div class="input-column">
                <select name="specialty" id="specialty">
                    
                </select>
            </div>
        </section>
        <section class="inner-section">
            <div class="label-column">
                <label for="medecin">Médecin :</label>
            </div>
            <div class="input-column">
                <select name="medecin" id="medecin">
                    
                </select>
            </div>
        </section>
    </div>
    <div class="tab-consultation">

    </div>
</template>
<style>
    
</style>