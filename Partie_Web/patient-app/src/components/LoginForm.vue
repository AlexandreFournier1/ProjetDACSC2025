<script setup>
    
</script>
<template>
    <form class="login-form">
        <section class="main-section">
            <h2>Login</h2>
            <section class="inner-section">
                <label for="nom">Nom :</label>
                <input type="text" id="nom" name="nom" required>
            </section>
            <section class="inner-section">
                <label for="prenom">Prénom :</label>
                <input type="text" id="prenom" name="prenom" required>
            </section>
            <section class="inner-section">
                <label for="id-patient">ID Patient :</label>
                <input type="text" id="id-patient" name="id-patient" required>
            </section>
            <section class="inner-section">
                <label for="new-patient">Nouveau Patient :</label>
                <input type="checkbox" id="new-patient" name="new-patient" value="new-patient">
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
        text-align: center;
        padding: 15px;
    }

    label {
        padding-right: 5px;
    }

    .inner-section {
        padding: 10px;
        font-size: large;
        font-weight: 400;
        text-align: justify;
    }

    input {
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