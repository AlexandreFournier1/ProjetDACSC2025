#ifndef CBP_H
#define CBP_H
// CBP = Consultation Booking Protocol

// Pas du tout sûr des structures
typedef struct {
    int id;
    char* name;
} Specialite;

typedef struct {
    int id;
    char* lastName;
    char* firstName;
} Docteur;

typedef struct {
	int id;
	Docteur medecin;
	Specialite specialite;
	char* date;
	char* heure;
} Consultation;

char* CBP_Login(char* nom, char* prenom, int numPatient);
char* CBP_Logout();

// Pas du tout sûr des prototypes des fonctions à partir d'ici (en tout cas la valeur de retour)
Specialite* CBP_GetSpecialites();
Docteur* CBP_GetDoctors();
Consultation* CBP_SearchConsultations(Specialite specialite, Docteur medecin, char* dateDebut, char* dateFin);
bool CBP_BookConsultation(int consulationId, char* reason);

#endif