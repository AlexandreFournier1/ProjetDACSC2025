#ifndef CBP_H
#define CBP_H
// CBP = Consultation Booking Protocol

#define NB_MAX_CLIENTS 100
#define MAX_SIZE_REQUETE 512
#define MAX_SIZE_REPONSE 255

bool CBP(char* requete, char* reponse, int socket);
char* CBP_Login(char* nom, char* prenom, int numPatient);
char* CBP_Logout(char* nom, char* prenom, int numPatient);
char* CBP_GetSpecialites();
char* CBP_GetDoctors();
char* CBP_SearchConsultations(int idSpecialite, int idMedecin, char* dateDebut, char* dateFin);
char* CBP_BookConsultation(int consulationId, char* reason);

#endif