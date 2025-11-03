#include "TCP.h"
#include <stdio.h> 
#include <unistd.h> 
#include <stdlib.h> 
#include <string.h>     
#include <sys/types.h> 
#include <sys/socket.h> 
#include <netdb.h> 

#include <arpa/inet.h>    // inet_pton
#include <netinet/in.h>   // sockaddr_in
#include <errno.h>

int ServerSocket(int port)
{
	int fd;

	printf("Creation d'un Socket\n");
	
	if ((fd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
	{
		perror("Erreur de socket()"); 
		return -1; 	
	}

	printf("Socket = %d\n", fd);

	//Construction Adresse
	struct addrinfo hints;
	struct addrinfo *Result;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV;

	char portStr[10];
	sprintf(portStr, "%d", port);

	if(getaddrinfo(NULL, portStr, &hints, &Result) != 0)
	{
		perror("Erreur de AddrInfo");
		close(fd);
		return -1;
	}

	char host[NI_MAXHOST];
	char port2[NI_MAXSERV];
	// struct addrinfo *Info;
	getnameinfo(Result->ai_addr, Result->ai_addrlen,
				host,NI_MAXHOST,port2,NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);

	printf("Mon Adresse IP: %s -- Mon Port: %s\n",host,port2);

	if(bind(fd, Result->ai_addr, Result->ai_addrlen) < 0)
	{
		perror("Erreur de bind()");
		close(fd);
		return -1;
	}

	freeaddrinfo(Result);
	printf("bind() reussi !\n"); 

	if(listen(fd, SOMAXCONN))
	{
		perror("Error de listen()");
		close(fd);
		return -1;
	}

	printf("listen() réussi\n");

	return fd;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int Accept(int sEcoute,char *ipClient)
{
	int fd;

	if((fd = accept(sEcoute, NULL, NULL)) == -1)
	{
		perror("Erreur de accept()");
		return -1;
	}

	printf("accept() reussi !"); 

	char host[NI_MAXHOST];
	char port[NI_MAXSERV];

	struct sockaddr_in adrClient;
	socklen_t adrClientLen = sizeof(struct sockaddr_in);
	getpeername(fd, (struct sockaddr*) &adrClient, &adrClientLen);
	getnameinfo((struct sockaddr*) &adrClient, adrClientLen,
				host, NI_MAXHOST,
				port, NI_MAXSERV,
				NI_NUMERICSERV | NI_NUMERICHOST);
	printf("Client Connect :: Adresse IP : %s  -- Port : %s\n", host,port);

	strncpy(ipClient, host, 16);

	return fd;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

// int ClientSocket(char* ipServeur,int portServeur)
// {
// 	int fdClient;

// 	if (strcmp(ipServeur, "0.0.0.0") == 0) 
// 	{
// 	    fprintf(stderr, "[ERREUR] '0.0.0.0' est une adresse invalide pour un client. Utilise '127.0.0.1' à la place.\n");
// 	    return -1;
// 	}

// 	printf("[DEBUG ClientSocket] IP Serveur = %s\n", ipServeur);
// 	printf("[DEBUG ClientSocket] Port Serveur = %d\n", portServeur);

// 	// Création de la socket

// 	printf("Creation d'un Socket Client\n");

// 	if ((fdClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
// 	{
// 		perror("Erreur de socket()");
// 		return -1;
// 	}

// 	printf("Socket cree = %d\n", fdClient);
	
// 	// Construction de l'adresse du serveur

// 	struct addrinfo hints;
// 	struct addrinfo *results = NULL;
// 	memset(&hints, 0, sizeof(struct addrinfo));

// 	printf("[DEBUG ClientSocket] Checkpoint 1\n");

// 	hints.ai_family = AF_INET;
// 	hints.ai_socktype = SOCK_STREAM;
// 	hints.ai_flags = AI_NUMERICSERV;

// 	printf("[DEBUG ClientSocket] Checkpoint 2\n");

// 	char portStr[10];
// 	sprintf(portStr, "%d", portServeur);

// 	int err = getaddrinfo(ipServeur, portStr, &hints, &results);
// 	if (err != 0)
// 	{
// 	    fprintf(stderr, "Erreur de getaddrinfo(): %s\n", gai_strerror(err));
// 	    close(fdClient);
// 	    return -1;
// 	}

// 	printf("[DEBUG ClientSocket] Résultats getaddrinfo :\n");
//     printf("  results = %p\n", (void*)results);

// 	printf("[DEBUG ClientSocket] Checkpoint 33\n");

//     printf("  ai_family = %d, ai_socktype = %d, ai_protocol = %d\n",results->ai_family, results->ai_socktype, results->ai_protocol);

//    	printf("[DEBUG ClientSocket] Checkpoint 4\n");

//     printf("  ai_addr = %p, ai_addrlen = %d\n", (void*)results->ai_addr, (int)results->ai_addrlen);

// 	printf("[DEBUG ClientSocket] Checkpoint 5\n");

// 	// Demande de connexion

// 	if (connect(fdClient, results->ai_addr, results->ai_addrlen) == -1)
// 	{
// 		printf("[DEBUG ClientSocket] Checkpoint dans connect\n");
// 		perror("Erreur de connect()");
// 		close(fdClient);
// 		return -1;
// 	}

// 	printf("[DEBUG ClientSocket] Checkpoint 6\n");

// 	printf("connect() reussi !\n");

// 	freeaddrinfo(results);

// 	return fdClient;
// }

int ClientSocket(char* ipServeur, int portServeur)
{
    if (!ipServeur) return -1;

    if (strcmp(ipServeur, "0.0.0.0") == 0) 
    {
        fprintf(stderr, "[ERREUR] '0.0.0.0' est une adresse invalide pour un client. Utilise '127.0.0.1' à la place.\n");
        return -1;
    }

    printf("[DEBUG ClientSocket] IP Serveur = %s\n", ipServeur);
    printf("[DEBUG ClientSocket] Port Serveur = %d\n", portServeur);

    int fdClient = socket(AF_INET, SOCK_STREAM, 0);
    if (fdClient == -1)
    {
        perror("Erreur de socket()");
        return -1;
    }
    printf("Socket cree = %d\n", fdClient);

    struct sockaddr_in srv;
    memset(&srv, 0, sizeof(srv));
    srv.sin_family = AF_INET;
    srv.sin_port = htons((uint16_t)portServeur);

    if (inet_pton(AF_INET, ipServeur, &srv.sin_addr) != 1)
    {
        fprintf(stderr, "Erreur inet_pton() pour l'adresse %s\n", ipServeur);
        close(fdClient);
        return -1;
    }

    // Tentative de connexion
    if (connect(fdClient, (struct sockaddr*)&srv, sizeof(srv)) == -1)
    {
        perror("Erreur de connect()");
        close(fdClient);
        return -1;
    }

    printf("connect() reussi !\n");
    return fdClient;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int Send(int sSocket,char* data,int taille)
{
	// Ecriture sur la socket

	int nb;

	data[taille] = '/';
	data[taille + 1] = '!';
	data[taille + 2] = '\0';

	if ((nb = write(sSocket, data, taille + 2)) == -1)
	{
		perror("Erreur de write()");
		return -1;
	}

	printf("Nombre ecrits = %d - Ecrit : --%s--\n", nb, data);

	return nb;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int Receive(int sSocket,char* data)
{
	// Lecture sur la socket
	int nb = 0, end = 0, firstCarac = 0, firstCaracPos = 0;
	char tmp;

	// Lecture caractère par caractère
	while(end == 0)
	{
		if (read(sSocket, &tmp, 1) == -1)
		{
			perror("Erreur de read()");
			return -1;
		}

		// On vérifie si on est potentiellement en fin de chaine
		if (tmp == '/')
		{
			firstCarac = 1;
			firstCaracPos = nb;
		}

		// Si on soupsonne une fin de chaine on vérifie la suite
		if (firstCarac == 1 && firstCaracPos != nb)
		{
			if (tmp == '!')
			{
				// On enlève le "/" de la chaine de caractères
				if (nb > 0) // On vérifie que nb > 0 au cas où que ce soit le début de la chaine de caractères
					nb--;

				data[nb] = '\0';
				end = 1;
			}
			else // Si on passe dans le else -> fausse alerte
				firstCarac = 0;
		}
		else
		{
			data[nb] = tmp;
			nb++;
		}
	}
	
	printf("Nombre lu = %d - Lu : --%s--\n", nb, data);

	return nb;
}