<script setup>
    import { ref } from 'vue';

    const nom = ref('');
    const prenom = ref('');
    const idPatient = ref('');
    const isNewPatient = ref(false);

    const error = ref('');

    function login() {
        if (isNewPatient.value === false && idPatient.value === '') {
            error.value = "Veuillez spécifier un ID ou cocher nouveau patient";
            return;
        }
    }

</script>
<template>
    <form class="login-form" @submit.prevent="login">
        <section class="error-section" v-if="error">
            <p class="error-text">
                {{ error }}
            </p>
        </section>
        <section class="main-section">
            <h2>Login</h2>
            <section class="inner-section">
                <div class="label-column">
                    <label for="nom">Nom :</label>
                </div>
                <div class="input-column">
                    <input v-model="nom" type="text" id="nom" name="nom" required>
                </div>
            </section>
            <section class="inner-section">
                <div class="label-column">
                    <label for="prenom">Prénom :</label>
                </div>
                <div class="input-column">
                    <input v-model="prenom" type="text" id="prenom" name="prenom" required>
                </div>
            </section>
            <section class="inner-section">
                <div class="label-column">
                    <label for="id-patient">ID Patient :</label>
                </div>
                <div class="input-column">
                    <input v-model="idPatient" type="text" id="id-patient" name="id-patient">
                </div>
            </section>
            <section class="inner-section">
                <div class="label-column">
                    <label for="new-patient">Nouveau Patient :</label>
                </div>
                <div class="input-column">
                    <input v-model="isNewPatient" type="checkbox" id="new-patient" name="new-patient" value="new-patient">
                </div>
            </section>
            <button type="submit">Se connecter</button>
        </section>
    </form>
</template>
<style scoped>
    .login-form {
        height: 100vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-family: Arial, Helvetica, sans-serif;
    }

    h2 {
        text-align: center;
    }

    .main-section {
        background-color: lightgreen;
        border-radius: 10px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        text-align: center;
        padding: 15px;
    }

    .inner-section {
        padding: 10px;
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

    .error-text {
        font-family: Arial, Helvetica, sans-serif;
        color: red;
        font-weight: bold;
    }

</style>