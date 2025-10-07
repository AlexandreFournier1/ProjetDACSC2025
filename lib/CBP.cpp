#include "CBP.h"
#include <mysql/mysql.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

// Liste des clients connectés
int clients[NB_MAX_CLIENTS];
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

////////////////////////////////////////////////////////////////////////////////////////////////////

// J'ai mis en commentaire parce qu'après réflexion, je pense que c'est useless de passé par ça parce que les fonctions font déjà tout le travail
// A voir ce que t'en penses
/*bool CBP(char* requete, char* reponse, int socket)
{
	char *ptr = strtok(requete, "#");

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

        printf("\t[THREAD %lu] LOGIN de %s %s\n", pthread_self(), nom, prenom);

        char rep[100] = CBP_Login(nom, prenom, atoi(numPatient));

        if (rep != NULL)
        	sprintf(reponse, rep);
	}

	// LOGOUT
	if (strcmp(ptr, "LOGOUT") == 0)
	{

	}

	// GETSPECIALITES
	if (strcmp(ptr, "GETSPECIALITES") == 0)
	{

	}

	// GETDOCTORS
	if (strcmp(ptr, "GETDOCTORS") == 0)
	{

	}

	// SEARCHCONSULTATIONS
	if (strcmp(ptr, "SEARCHCONSULTATIONS") == 0)
	{

	}

	// BOOKCONSULTATION
	if (strcmp(ptr, "BOOKCONSULTATION") == 0)
	{

	}

	return true;
}*/

////////////////////////////////////////////////////////////////////////////////////////////////////

// Pas encore tester
char* CBP_Login(char* nom, char* prenom, int numPatient) // numPatient = -1 si nouveau Patient
{

  	if (numPatient == -1)
  	{
  		sprintf(requete,"insert into patients (last_name, first_name) values ('%s', '%s');", nom, prenom);
  		AccesBD(requete);
  	}

  	sprintf(requete,"select id from patients where last_name = '%s' and first_name = '%s' and id = '%d';", nom, prenom, numPatient);

  	char* resultat = AccesBD(requete);
	
	if (resultat)
		if (numPatient == -1)
			sprintf(rep, "LOGIN#ok#%s", resultat);
		else
			sprintf(rep, "LOGIN#ok");
		
	else
		sprintf(rep, "LOGIN#ko");

	return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

// Pas encore tester
char* CBP_Logout(char* nom, char* prenom, int numPatient)
{
    sprintf(requete, "LOGOUT#%s#%s#%s", nom, prenom, numPatient);

	return requete;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

// Pas encore tester
char* CBP_GetSpecialites()
{
	sprintf(requete, "select * from specialties;");

	char *reponse = AccesBD(requete);

	if (reponse)
		sprintf(rep, "GETSPECIALITES#%s", reponse);
	else
		sprintf(rep, "GETSPECIALITES#Pas de Specialites trouvees !");

	return rep;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_GetDoctors()
{
	return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_SearchConsultations(int idSpecialite, int idMedecin, char* dateDebut, char* dateFin)
{
	sprintf(requete, "select c.id, m.last_name, m.first_name, s.name, c.date, c.hour from consultations c inner join doctors m on doctors.id = consulations.doctor_id inner join specialties s on specialties.id = consulations.specialty_id where s.id = '%s' and m.id = '%s' and ...", idSpecialite, idMedecin);

	return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_BookConsultation(int consulationId, char* reason)
{
	return 0;
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
        if (clients[i] == socket)
        {
            indice = i;
            break;
        }
    }

    pthread_mutex_unlock(&mutexClients);

    return indice;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

void AddClient(int socket)
{
    pthread_mutex_lock(&mutexClients);

    clients[nbClients] = socket;
    nbClients++;

    pthread_mutex_unlock(&mutexClients);
}

///////////////////////////////////////////////²/////////////////////////////////////////////////////

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