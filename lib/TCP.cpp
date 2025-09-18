#include "TCP.h"

int ServerSocket(int port)
{
	int fd;

	printf("Creation d'un Socket\n");
	
	if ((fd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
	{
		perror("Erreur de socket()\n"); 
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
		perror("Erreur de AddrInfo\n");
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
		perror("Erreur de bind()\n");
		return -1;
	}

	freeaddrinfo(Result);
	printf("bind() reussi !\n"); 

	if(listen(fd, SOMAXCONN))
	{
		perror("Error de listen()\n");
		return -1;
	}

	printf("listen() réussi\n");

	return fd;
}


int Accept(int sEcoute,char *ipClient)
{
	int fd;

	if((fd = accept(sEcoute, NULL, NULL)) == -1)
	{
		perror("Erreur de accept()\n");
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

int ClientSocket(char* ipServeur,int portServeur)
{
	return 0;
}

int Send(int sSocket,char* data,int taille)
{
	return 0;
}

int Receive(int sSocket,char* data)
{
	return 0;
}