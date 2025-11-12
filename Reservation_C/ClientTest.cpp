#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <sys/socket.h>

#define MAX_SIZE_REQUETE 256
#define MAX_SIZE_REPONSE 256

// Socket Globale
int sfd = -1;

void HandlerSIGINT(int sig);
void Echange(int sock, char *requete, char *reponse);

int main(int argc, char* argv[])
{
    if (argc < 4) 
    {
        printf("Usage : %s id nom prenom\n", argv[0]);
        exit(1);
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

    struct addrinfo hints, *res;
    char requete[MAX_SIZE_REQUETE];
    char reponse[MAX_SIZE_REPONSE];
    char ip[] = "127.0.0.1";
    char port[] = "50000";

    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;

    if (getaddrinfo(ip, port, &hints, &res) != 0)
    {
        perror("getaddrinfo");
        return 1;
    }

    sfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    if (sfd < 0)
    {
        perror("socket");
        return 1;
    }

    printf("[DEBUG] Tentative de connexion à %s:%s...\n", ip, port);

    if (connect(sfd, res->ai_addr, res->ai_addrlen) == -1)
    {
        perror("connect");
        freeaddrinfo(res);
        return 1;
    }

    printf("[INFO] Connecté au serveur !\n");

    // Construction de la requête LOGIN
    int patientId = atoi(argv[1]);
    char *nom = argv[2];
    char *prenom = argv[3];
    
    sprintf(requete, "LOGIN#%s#%s#%d", nom, prenom, patientId);

    Echange(sfd, requete, reponse);

    char *token = strtok(reponse, "#");
    if (token && strcmp(token, "LOGIN") == 0)
    {
        char *status = strtok(NULL, "#");
        if (status && strcmp(status, "ok") == 0)
        {
            printf("[INFO] Login réussi\n");
        }
        else
        {
            printf("[INFO] Échec du login\n");
        }
    }

    printf("[INFO] Le client reste connecté (Ctrl+C pour quitter)...\n");

    // On attend simplement un Ctrl+C
    while (1)
    {
        pause();
    }

    freeaddrinfo(res);
    return 0;
}

// Gestion du signal Ctrl+C
void HandlerSIGINT(int sig)
{
    printf("\n[INFO] Fermeture du client...\n");

    if (sfd != -1)
        close(sfd);

    exit(0);
}

void Echange(int sock, char *requete, char *reponse)
{
    ssize_t n;

    // Envoi
    n = write(sock, requete, strlen(requete));
    if (n <= 0)
    {
        perror("[ERREUR] write");
        exit(1);
    }
    printf("[DEBUG] Requête envoyée : %s\n", requete);

    // Réception
    n = read(sock, reponse, MAX_SIZE_REPONSE - 1);
    if (n <= 0)
    {
        perror("[ERREUR] read");
        exit(1);
    }

    reponse[n] = '\0';
    printf("[DEBUG] Réponse reçue : %s\n", reponse);
}