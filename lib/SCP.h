#ifndef SCP_H
#define SCP_H

// SCP = Simple Communication Protocol

#define PORT_RESERVATION 50000
#define PORT_ADMIN 60000

#define NB_THREADS_POOL 2 
#define TAILLE_FILE_ATTENTE 20 
#define NB_MAX_CLIENTS 100

bool SCP(char* requete, char* reponse,int socket); 
bool SCP_Login(const char* user,const char* password); 
int  SCP_Operation(char op,int a,int b); 
void SCP_Close(); 

#endif 