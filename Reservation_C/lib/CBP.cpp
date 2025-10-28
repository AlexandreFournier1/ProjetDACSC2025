#include "CBP.h"
#include <mysql/mysql.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

// Liste des clients connectés
char* clients[NB_MAX_CLIENTS];
int nbClients = 0;

// Mutex de protection
pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

// erreur potentiel si en meme temps
// Solution mutex
char requete[MAX_SIZE_REQUETE], rep[MAX_SIZE_REPONSE];

// Prototypes internes
char* AccesBD(char* requete);
int  isPresent(int socket);
void AddClient(int socket);
void RemoveClient(int socket);
int getSpecialiteId(char* specialiteName);
int getDoctorId(char* doctorLastName, char* doctorFirstName);
int getPatientId(char* nom, char* prenom);

////////////////////////////////////////////////////////////////////////////////////////////////////

bool CBP(char* requete, char* reponse, int socket)
{
	char temp[MAX_SIZE_REQUETE];
	strcpy(temp, requete);

	char *ptr = strtok(temp, "#");

	// LOGIN
	if (strcmp(ptr, "LOGIN") == 0)
	{
		if (isPresent(socket) >= 0)
		{
			sprintf(reponse, "LOGIN#ko#Client déjà loggé !");
            return false;
		}

		char nom[50], prenom[50], numPatient[50];
		strcpy(nom, strtok(NULL, "#"));
        strcpy(prenom, strtok(NULL, "#"));
        strcpy(numPatient, strtok(NULL, "#"));

        char infoClient[MAX_SIZE_REQUETE];

		sprintf(infoClient, "%d;%s;%s;%d", socket, nom, prenom, atoi(numPatient));

		AddClient(infoClient);

        printf("\t[THREAD %lu] LOGIN de %s %s\n", pthread_self(), nom, prenom);

        char* rep = CBP_Login(nom, prenom, atoi(numPatient));

        if (strcmp(rep, "") == 0)
        	printf("\t[THREAD %lu] Erreur de CBP_Login\n", pthread_self());

        if (rep != NULL)
        	sprintf(reponse, "%s", rep);
	}

	// LOGOUT
	if (strcmp(ptr, "LOGOUT") == 0)
	{
		char nom[50], prenom[50], numPatient[50];
		strcpy(nom, strtok(NULL, "#"));
        strcpy(prenom, strtok(NULL, "#"));
        strcpy(numPatient, strtok(NULL, "#"));

        char infoClient[MAX_SIZE_REQUETE];

		sprintf(infoClient, "%d;%s;%s;%d", socket, nom, prenom, atoi(numPatient));

		RemoveClient(infoClient);

        printf("\t[THREAD %lu] LOGOUT de %s %s\n", pthread_self(), nom, prenom);

        char* rep = CBP_Logout(nom, prenom, atoi(numPatient));

        if (strcmp(rep, "") == 0)
        	printf("\t[THREAD %lu] Erreur de CBP_Logout\n", pthread_self());

        if (rep != NULL)
        	sprintf(reponse, "%s", rep);
	}

	// GETSPECIALITES
	if (strcmp(ptr, "GETSPECIALITES") == 0)
	{
		char* rep = CBP_GetSpecialites();
		if (rep == NULL || strcmp(rep, "") == 0)
			printf("\t[THREAD %lu] Erreur de CBP_GetSpecialites\n", pthread_self());
		else
			sprintf(reponse, "%s", rep);

		free(rep);
	}

	// GETDOCTORS
	if (strcmp(ptr, "GETDOCTORS") == 0)
	{
	    char* rep = CBP_GetDoctors();  // alloue dynamiquement
	    if (rep == NULL || strcmp(rep, "") == 0)
	        printf("\t[THREAD %lu] Erreur de CBP_GetDoctors\n", pthread_self());
	    else
	        sprintf(reponse, "%s", rep); // copie dans le buffer passé en argument

	    free(rep);
	}

	// SEARCHCONSULTATIONS
	if (strcmp(ptr, "SEARCHCONSULTATIONS") == 0)
	{
		char specialiteName[50], medecinFullName[50], medecinLastName[50], medecinFirstName[50], dateDebut[50], dateFin[50];
		strcpy(specialiteName, strtok(NULL, "#"));
        strcpy(medecinFullName, strtok(NULL, "#"));

		char *space = strchr(medecinFullName, ' ');

		if (space != NULL) 
		{
		    *space = '\0';
		    strcpy(medecinLastName, medecinFullName);
		    strcpy(medecinFirstName, space + 1);
		}

		char *tmp = strtok(NULL, "#");
		if (tmp != NULL) 
			strcpy(dateDebut, tmp);

		tmp = strtok(NULL, "#");
		if (tmp != NULL) 
			strcpy(dateFin, tmp);

        int idSpecialite = getSpecialiteId(specialiteName);
       	int idMedecin = getDoctorId(medecinLastName, medecinFirstName);

        char* rep = CBP_SearchConsultations(idSpecialite, idMedecin, dateDebut, dateFin);

        if (strcmp(rep, "") == 0)
    		printf("\t[THREAD %lu] Erreur de CBP_SearchConsultations\n", pthread_self());

        if (rep != NULL)
        	sprintf(reponse, "%s", rep);
	}

	// BOOKCONSULTATION
	if (strcmp(ptr, "BOOKCONSULTATION") == 0)
	{
		char idconsult[10], nom[50], prenom[50], reason[100];
		strcpy(idconsult, strtok(NULL, "#"));
        strcpy(nom, strtok(NULL, "#"));
        strcpy(prenom, strtok(NULL, "#"));
        strcpy(reason, strtok(NULL, "#"));

        printf("Après : strcpy\n");

        int id = atoi(idconsult);
	    printf("Avant : CBP_BookConsultation\n");

	    char* rep = CBP_BookConsultation(id, nom, prenom, reason);
	    printf("Après : CBP_BookConsultation\n");

        if(strcmp(rep, "") == 0)
           	printf("\t[THREAD %lu] Erreur de CBP_BookConsultation\n", pthread_self());

        if (rep != NULL)
        	sprintf(reponse, "%s", rep);
	}

	// GETPATIENTCONNECTE
	if (strcmp(ptr, "GETPATIENTCONNECTE") == 0)
	{
		char* rep = getPatientConnecte();
		
		sprintf(reponse, "%s", rep);
	}

	return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_Login(char* nom, char* prenom, int numPatient) // numPatient = -1 si nouveau Patient
{
	char requete[MAX_SIZE_REQUETE]/*, rep[MAX_SIZE_REPONSE]*/;
	char* rep = (char*)malloc(MAX_SIZE_REPONSE);
	char* id = (char*)malloc(MAX_SIZE_REPONSE);

  	if (numPatient == -1)
  	{
  		sprintf(requete,"INSERT INTO patients (last_name, first_name) VALUES ('%s', '%s');", nom, prenom);

  		AccesBD(requete);

  		sprintf(requete,"SELECT id FROM patients WHERE last_name = '%s' AND first_name = '%s';", nom, prenom);

  		id = AccesBD(requete);
  	}
  	else
  		sprintf(id, "%d", numPatient);

  	sprintf(requete,"SELECT id FROM patients WHERE last_name = '%s' AND first_name = '%s' AND id = '%s';", nom, prenom, id);

  	char* resultat = AccesBD(requete);
	
	if (resultat != NULL && strcmp(resultat, "") != 0)
		if (numPatient == -1)
			sprintf(rep, "LOGIN#ok#%s", resultat);
		else
			sprintf(rep, "LOGIN#ok");
	else
		sprintf(rep, "LOGIN#ko");


	return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_Logout(char* nom, char* prenom, int numPatient)
{
	char* reponse = (char*)malloc(MAX_SIZE_REPONSE);

    sprintf(reponse, "LOGOUT#ok#%s#%s#%d", nom, prenom, numPatient);

	return reponse;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_GetSpecialites()
{
	char requete[MAX_SIZE_REQUETE];
	char* rep = (char*)malloc(MAX_SIZE_REPONSE);

	sprintf(requete, "SELECT * FROM specialties;");

	char *reponse = AccesBD(requete);

	if (reponse && strlen(reponse) > 0)
		sprintf(rep, "GETSPECIALITES#%s", reponse);
	else
		sprintf(rep, "GETSPECIALITES#Pas de Specialites trouvees !");

	return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_GetDoctors()
{
    char requete[MAX_SIZE_REQUETE];
    char* rep = (char*)malloc(MAX_SIZE_REPONSE);

    sprintf(requete, "SELECT id, last_name, first_name FROM doctors;");

    char *reponse = AccesBD(requete);

    if (reponse && strlen(reponse) > 0)
    {
        sprintf(rep, "GETDOCTORS#%s", reponse);
    }
    else
    {
        sprintf(rep, "GETDOCTORS#Pas de docteurs trouvés !");
    }

    return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_SearchConsultations(int idSpecialite, int idMedecin, char* dateDebut, char* dateFin)
{
	char requete[MAX_SIZE_REQUETE];
	char* rep = (char*)malloc(MAX_SIZE_REPONSE);

	sprintf(requete,
	    "SELECT c.id, m.last_name, m.first_name, s.name, c.date, c.hour "
	    "FROM consultations c "
	    "INNER JOIN doctors m ON m.id = c.doctor_id "
	    "INNER JOIN specialties s ON s.id = m.specialty_id "
	    "WHERE s.id = %d AND m.id = %d AND c.patient_id == NULL "
	    "AND c.date BETWEEN '%s' AND '%s';",
	    idSpecialite, idMedecin, dateDebut, dateFin);

	char* reponse = AccesBD(requete);

	if (reponse != NULL && strcmp(reponse, "") != 0)
		sprintf(rep, "SEARCHCONSULTATIONS#%s", reponse);
	else
		sprintf(rep, "SEARCHCONSULTATIONS#Pas de Consultations trouvees !");

	return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_BookConsultation(int consultationId, char* nom, char* prenom, char* reason)
{
	printf("Debut : CBP_BookConsultationn");
	char requete[MAX_SIZE_REQUETE]/*, rep[MAX_SIZE_REPONSE]*/;
	char* rep = (char*)malloc(MAX_SIZE_REPONSE);

    int idpatient = getPatientId(nom, prenom);
	printf("Après : CBP_BookConsultationidpatient\n");

	sprintf(requete, "UPDATE consultations SET reason = '%s', patient_id = '%d' WHERE id = '%d';", reason, idpatient, consultationId);

	AccesBD(requete);
    printf("Après : CBP_BookConsultationreponse\n");

	sprintf(rep, "BOOKCONSULTATION#ok");
	printf("Après : CBP_BookConsultation#OK\n");


	return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

void CBP_Close()
{
    pthread_mutex_lock(&mutexClients);

    for (int i = 0; i < nbClients; i++)
        close(clients[i]);

    pthread_mutex_unlock(&mutexClients);
}

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

char* AccesBD(char* requete)
{
	char* reponse = NULL;

	// Connection à la BD
  	MYSQL* connexion = mysql_init(NULL);
  	mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0);

  	mysql_query(connexion,requete);

    MYSQL_RES* resultat = mysql_store_result(connexion);

    // Si la requête est un SELECT (sinon on a rien à renvoyer)
    if (resultat) 
    {
        MYSQL_ROW row;
        int nbCols = mysql_num_fields(resultat);

        reponse = (char*)malloc(4096);
        reponse[0] = '\0';

        while ((row = mysql_fetch_row(resultat))) 
        {
            for (int i = 0; i < nbCols; i++) 
            {
                strcat(reponse, row[i]);
                if (i < nbCols - 1) 
                	strcat(reponse, ";"); // Séparateur pour les éléments de la même ligne
            }
            strcat(reponse, "#"); // Séparateur pour les différents éléments
        }

        mysql_free_result(resultat);
    }

    mysql_close(connexion);

    return reponse;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int isPresent(int socket)
{
    int indice = -1;

    pthread_mutex_lock(&mutexClients);

    for (int i = 0; i < nbClients; i++)
    {
    	char temp[MAX_SIZE_REQUETE];
		strcpy(temp, clients[i]);

		char *ptr = strtok(temp, ";");

        if (strcmp(ptr, socket) == 0)
        {
            indice = i;
            break;
        }
    }

    pthread_mutex_unlock(&mutexClients);

    return indice;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

void AddClient(int socket, char* infoClient)
{
    pthread_mutex_lock(&mutexClients);

    strcpy(clients[nbClients], infoClient)

    //clients[nbClients] = socket;
    nbClients++;

    pthread_mutex_unlock(&mutexClients);
}

////////////////////////////////////////////////////////////////////////////////////////////////////

void RemoveClient(int socket)
{
    int pos = isPresent(socket);
    if (pos == -1) return;

    pthread_mutex_lock(&mutexClients);

    for (int i = pos; i <= nbClients - 2; i++)
        clients[i] = clients[i + 1];

    nbClients--;

    pthread_mutex_unlock(&mutexClients);
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* getPatientConnecte()
{
	char* temp[MAX_SIZE_REPONSE];

	pthread_mutex_lock(&mutexClients);

	for (int i = 0; i < nbClients; i++)
	{
		strcat(clients[i], "#");
	}

	pthread_mutex_unlock(&mutexClients);
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int getSpecialiteId(char* specialiteName)
{
	char* requete = (char*)malloc(MAX_SIZE_REQUETE);

	sprintf(requete, "SELECT id FROM specialties WHERE name = '%s';", specialiteName);
    
    char *reponse = AccesBD(requete);

    int id = atoi(reponse);

    free(requete);

    return id;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int getDoctorId(char* doctorLastName, char* doctorFirstName)
{
    char* requete= (char*)malloc(MAX_SIZE_REQUETE);

	sprintf(requete, "SELECT id FROM doctors WHERE last_name = '%s' and first_name = '%s';", doctorLastName, doctorFirstName);
    
    char *reponse = AccesBD(requete);

    int id = atoi(reponse);

    free(requete);

    return id;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

int getPatientId(char* nom, char* prenom)
{
    char* requete= (char*)malloc(MAX_SIZE_REQUETE);

	sprintf(requete, "SELECT id FROM patients WHERE last_name = '%s' and first_name = '%s';", nom, prenom);

	char *reponse = AccesBD(requete);

	int id = atoi(reponse);

	return id;
}