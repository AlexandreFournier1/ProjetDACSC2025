#ifndef CBP_H
#define CBP_H
// CBP = Consultation Booking Protocol

#define MAX_SIZE_REQUETE 512
#define MAX_SIZE_REPONSE 4096

#define PORT_RESERVATION 50000
#define PORT_ADMIN 60000

#define NB_THREADS_POOL 5 
#define TAILLE_FILE_ATTENTE 20 
#define NB_MAX_CLIENTS 100

bool CBP(char* requete, char* reponse, int socket);
char* CBP_Login(char* nom, char* prenom, int numPatient);
char* CBP_Logout(char* nom, char* prenom, int numPatient);
char* CBP_GetSpecialites();
char* CBP_GetDoctors();
char* CBP_SearchConsultations(int idSpecialite, int idMedecin, char* dateDebut, char* dateFin);
char* CBP_BookConsultation(int consulationId, char* reason);
char* CBP_BookConsultation(int consultationId, char* nom, char* prenom, char* reason);
void CBP_Close();

// Fonctions supplémentaires
char* getPatientConnecte();

#endif