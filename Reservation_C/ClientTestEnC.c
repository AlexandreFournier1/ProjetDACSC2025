// Pour Compiler : make

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netdb.h>
#include <sys/socket.h>
#include <signal.h>

#include "./lib/TCP.h"

#define MAX_SIZE_REQUETE 256
#define MAX_SIZE_REPONSE 256

int sfd = -1;

void HandlerSIGINT(int sig);
void Echange(char* requete, char* reponse);

int main(int argc, char *argv[]) 
{
    if (argc < 4) 
    {
        printf("Usage: %s id prenom nom\n", argv[0]);
        return 1;
    }

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

    char *id = argv[1];
    char *prenom = argv[2];
    char *nom = argv[3];

    if ((sfd = ClientSocket("127.0.0.1", 50000)) == -1)
    {
        perror("Erreur de ClientSocket");
        exit(1);
    }
    
    printf("Connecte sur le serveur.\n");

    // struct addrinfo hints, *res;
    // memset(&hints, 0, sizeof(hints));
    // hints.ai_family = AF_INET;
    // hints.ai_socktype = SOCK_STREAM;

    // if (getaddrinfo("127.0.0.1", "50000", &hints, &res) != 0) 
    // {
    //     perror("getaddrinfo");
    //     return 1;
    // }

    // sfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    // if (sfd < 0) { perror("socket"); return 1; }

    // if (connect(sfd, res->ai_addr, res->ai_addrlen) == -1) {
    //     perror("connect");
    //     close(sfd);
    //     return 1;
    // }

    // printf("Connecté au serveur.\n");

    char requete[MAX_SIZE_REQUETE];
    char reponse[MAX_SIZE_REPONSE];

    snprintf(requete, sizeof(requete), "LOGIN#%s#%s#%s", nom, prenom, id);

    Echange(requete, reponse);

    // if (write(sfd, requete, strlen(requete)) == -1) 
    // {
    //     perror("write");
    //     close(sfd);
    //     return 1;
    // }

    // int n = read(sfd, reponse, sizeof(reponse)-1);

    // if (n < 0) 
    // {
    //     perror("read");
    // } 
    // else 
    // {
    //     reponse[n] = '\0';
    //     printf("Réponse du serveur : %s\n", reponse);
    // }

    printf("Réponse du serveur : %s\n", reponse);

    printf("Appuyez sur Ctrl+C pour quitter et fermer le client.\n");

    while (1) 
        pause(); // attend un signal

    close(sfd);
    //freeaddrinfo(res);
    return 0;
}

void HandlerSIGINT(int sig) 
{
    if (sfd != -1) close(sfd);
    printf("\nClient arrêté.\n");
    exit(0);
}

void Echange(char* requete, char* reponse)
{
    int nbEcrits, nbLus;

    // Envoi de la requete
    if ((nbEcrits = Send(sfd, requete, strlen(requete))) == -1)
    {
        perror("Erreur de Send");
        close(sfd);
        exit(1);
    }

    // Attente de la reponse
    if ((nbLus = Receive(sfd, reponse)) < 0)
    {
        perror("Erreur de Receive");
        close(sfd);
        exit(1);
    }

    if (nbLus == 0)
    {
        printf("Serveur arrete, pas de reponse reçue...\n");
        close(sfd);
        exit(1);
    }

    reponse[nbLus] = 0;
}