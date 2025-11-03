#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>

#include "./lib/TCP.h"
#include "./lib/CBP.h"
#include "./lib/ACBP.h"

// Prototypes
void HandlerSIGINT(int s);
void TraitementConnexion(int sService);
void TraitementConnexionAdmin(int sService);
void* FctThreadClient(void* p);
void* FctThreadClientALaDemande(void* p);

void* FctThreadEcouteClient(void* p);
void* FctThreadEcouteAdmin(void* p);

// Variables globales
int sEcoute;
int sEcouteAdmin;

// Gestion du pool de threads
int socketsAcceptees[TAILLE_FILE_ATTENTE];
int indiceEcriture = 0, indiceLecture = 0;
pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t  condSocketsAcceptees;

int main(int argc, char* argv[])
{
    // Initialisation socketsAcceptees
    pthread_mutex_init(&mutexSocketsAcceptees, NULL);
    pthread_cond_init(&condSocketsAcceptees, NULL);

    for (int i = 0; i < TAILLE_FILE_ATTENTE; i++)
        socketsAcceptees[i] = -1;

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;

    if (sigaction(SIGINT, &A, NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }

    // Création des deux sockets
    if ((sEcoute = ServerSocket(PORT_RESERVATION)) == -1)
    {
        perror("Erreur de ServeurSocket");
        exit(1);
    }

    if ((sEcouteAdmin = ServerSocket(PORT_ADMIN)) == -1)
    {
        perror("Erreur de ServeurSocket");
        exit(1);
    }

    // Création des threads d'écoutes
    pthread_t threadsClient, threadsAdmin;
    pthread_create(&threadsClient, NULL, FctThreadEcouteClient, NULL);
    pthread_create(&threadsAdmin, NULL, FctThreadEcouteAdmin, NULL);

    printf("Démarrage du serveur.\n");
    printf("  - Port client : %d\n", PORT_RESERVATION);
    printf("  - Port admin  : %d\n", PORT_ADMIN);

    pthread_join(threadsClient, NULL);
    pthread_join(threadsAdmin, NULL);

    return 0;
}


// Thread qui gère le port client (50000)
void* FctThreadEcouteClient(void* p)
{
    // Création du pool de threads
    printf("Création du pool de threads.\n");
    pthread_t th;
    for (int i = 0; i < NB_THREADS_POOL; i++)
        pthread_create(&th, NULL, FctThreadClient, NULL);

    int sService;
    char ipClient[50];

    while (1)
    {
        printf("Attente d'une connexion CLIENT...\n");

        if ((sService = Accept(sEcoute, ipClient)) == -1)
        {
            perror("Erreur de Accept");
            close(sEcoute);
            CBP_Close();
            exit(1);
        }

        printf("Connexion CLIENT acceptée : IP=%s socket=%d\n", ipClient, sService);

        // Insertion en liste d'attente et réveil d'un thread du pool
        pthread_mutex_lock(&mutexSocketsAcceptees);
        socketsAcceptees[indiceEcriture] = sService;

        indiceEcriture++;
        if (indiceEcriture == TAILLE_FILE_ATTENTE)
            indiceEcriture = 0;

        pthread_mutex_unlock(&mutexSocketsAcceptees);
        pthread_cond_signal(&condSocketsAcceptees);
    }
}

// Thread qui gère le port admin (60000)
void* FctThreadEcouteAdmin(void* p)
{
    int sService; 
    pthread_t th; 
    char ipClient[50];

    while (1)
    {
        printf("Attente d'une connexion ADMIN...\n");

        if ((sService = Accept(sEcouteAdmin, ipClient)) == -1)
        {
            perror("Erreur de Accept");
            close(sEcouteAdmin);
            CBP_Close();
            exit(1);
        }

        printf("Connexion ADMIN acceptée : IP=%s socket=%d\n", ipClient, sService);

        // Creation d'un thread "client" s'occupant du client connecté 
        int *p = (int*)malloc(sizeof(int)); 
        *p = sService;

        pthread_create(&th,NULL,FctThreadClientALaDemande,(void*)p);

        pthread_detach(th);
    }
}

// Fonction exécutée par les threads du pool
void* FctThreadClient(void* p)
{
    int sService;

    while (1)
    {
        printf("\t[THREAD %lu] Attente socket...\n", pthread_self());

        // Attente d'une tâche
        pthread_mutex_lock(&mutexSocketsAcceptees);

        while (indiceEcriture == indiceLecture)
            pthread_cond_wait(&condSocketsAcceptees, &mutexSocketsAcceptees);

        sService = socketsAcceptees[indiceLecture];
        socketsAcceptees[indiceLecture] = -1;

        indiceLecture++;
        if (indiceLecture == TAILLE_FILE_ATTENTE)
            indiceLecture = 0;

        pthread_mutex_unlock(&mutexSocketsAcceptees);

        // Traitement de la connexion
        printf("\t[THREAD %lu] Je m'occupe de la socket %d\n", pthread_self(), sService);
        TraitementConnexion(sService);
    }
}

// Fonction exécutée par les threads à la demande
void* FctThreadClientALaDemande(void* p)
{  
    int sService = *((int*)p); 
    free(p); 
    printf("\t[THREAD %p] Je m'occupe de la socket %d\n",pthread_self(),sService);

    TraitementConnexion(sService); 

    pthread_exit(NULL);
}

// Gestion du signal SIGINT (CTRL+C)
void HandlerSIGINT(int s)
{
    printf("\nArrêt du serveur.\n");

    close(sEcoute);
    close(sEcouteAdmin);

    pthread_mutex_lock(&mutexSocketsAcceptees);
    for (int i = 0; i < TAILLE_FILE_ATTENTE; i++)
        if (socketsAcceptees[i] != -1)
            close(socketsAcceptees[i]);
    pthread_mutex_unlock(&mutexSocketsAcceptees);

    CBP_Close();
    exit(0);
}

// Traitement d'une connexion client
#define MAX_TAILLE 4096

void TraitementConnexion(int sService)
{
    char requete[MAX_TAILLE], reponse[MAX_TAILLE];
    int nbLus, nbEcrits;
    bool onContinue = true;

    while (onContinue)
    {
        memset(requete, 0, sizeof(requete));
        memset(reponse, 0, sizeof(reponse));

        printf("\t[THREAD %lu] Attente requête...\n", pthread_self());

        // Réception de la requête
        nbLus = Receive(sService, requete);
        if (nbLus < 0)
        {
            perror("Erreur de Receive");
            close(sService);
            HandlerSIGINT(0);
        }

        // Fin de connexion ?
        if (nbLus == 0)
        {
            printf("\t[THREAD %lu] Fin de connexion du client.\n", pthread_self());
            close(sService);
            return;
        }

        requete[nbLus] = '\0';
        printf("\t[THREAD %lu] Requête reçue = %s\n", pthread_self(), requete);

        // Traitement de la requête
        onContinue = CBP(requete, reponse, sService);

        if (strlen(reponse) == 0)
            strcpy(reponse, "ERREUR#Réponse vide du serveur");

        // Envoi de la réponse
        nbEcrits = Send(sService, reponse, strlen(reponse));
        if (nbEcrits < 0)
        {
            perror("Erreur de Send");
            close(sService);
            HandlerSIGINT(0);
        }

        printf("\t[THREAD %lu] Réponse envoyée = %s\n", pthread_self(), reponse);

        if (!onContinue)
        {
            printf("\t[THREAD %lu] Fin de connexion de la socket %d\n", pthread_self(), sService);
            close(sService);
            return;
        }
    }
}

void TraitementConnexionAdmin(int sService)
{
    char requete[200], reponse[200]; 
    int nbLus, nbEcrits; 
    bool onContinue = true;

    while (onContinue) 
    { 
        printf("\t[THREAD %p] Attente requete...\n",pthread_self()); 

        // Réception de la requête
        if ((nbLus = Receive(sService,requete)) < 0) 
        { 
            perror("Erreur de Receive"); 
            close(sService); 
            HandlerSIGINT(0); 
        }

        // Fin de connexion ?
        if (nbLus == 0) 
        { 
            printf("\t[THREAD %p] Fin de connexion du client.\n",pthread_self()); 
            close(sService); 
            return; 
        }

        requete[nbLus] = 0;

        printf("\t[THREAD %p] Requete recue = %s\n",pthread_self(),requete);

        // Traitement de la requete
        onContinue = ACBP(requete,reponse,sService);

        // Envoi de la reponse
        if ((nbEcrits = Send(sService,reponse,strlen(reponse))) < 0) 
        { 
            perror("Erreur de Send"); 
            close(sService); 
            HandlerSIGINT(0); 
        }

        printf("\t[THREAD %p] Reponse envoyee = %s\n",pthread_self(),reponse);

        if (!onContinue)  
            printf("\t[THREAD %p] Fin de connexion de la socket %d\n",pthread_self(),sService); 
    }
}