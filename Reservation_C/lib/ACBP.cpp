#include "ACBP.h"
#include "CBP.h"
#include <mysql/mysql.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

bool ACBP(char* requete, char* reponse, int socket)
{
	char temp[MAX_SIZE_REQUETE];
	strcpy(temp, requete);

	char *ptr = strtok(temp, "#");

	sprintf(reponse, "ERREUR");

	// LIST_CLIENTS
	if (strcmp(temp, "LIST_CLIENTS") == 0)
	{
		char* temp = (char*)malloc(4096);
		char* rep;

		temp = getPatientConnecte(); // Format : ip;id

		//strcpy(temp, "192.168.2.128;1#192.168.2.128;2#192.168.2.128;3");

		rep = ACBP_LIST_CLIENTS(temp);

		sprintf(reponse, "%s", rep);

		free(temp);

		return true;
	}

	return false;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* ACBP_LIST_CLIENTS(char* patients)
{
    if (patients == NULL || strlen(patients) == 0)
        return NULL;

    char* reponse = (char*)malloc(4096);
    reponse[0] = '\0'; // vide au départ

    char tmp[4096];
    strcpy(tmp, patients);

    MYSQL* connexion = mysql_init(NULL);
    mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0);

    char *saveptr1, *saveptr2;
    char *ligne = strtok_r(tmp, "#", &saveptr1);

    while (ligne != NULL)
    {
        char *ip   = strtok_r(ligne, ";", &saveptr2);  
        char *id   = strtok_r(NULL, ";", &saveptr2);

        char requeteSQL[256];
        sprintf(requeteSQL, "SELECT last_name, first_name FROM patients WHERE id = %s;", id);

        mysql_query(connexion, requeteSQL);

        MYSQL_RES* resultat = mysql_store_result(connexion);
        MYSQL_ROW row;

        while ((row = mysql_fetch_row(resultat)))
        {
        	strcat(reponse, ip);
        	strcat(reponse, ";");

        	strcat(reponse, id);
        	strcat(reponse, ";");

            strcat(reponse, row[0]);
            strcat(reponse, ";");

            strcat(reponse, row[1]);   

            strcat(reponse, "#");
        }

        mysql_free_result(resultat);
        ligne = strtok_r(NULL, "#", &saveptr1);
    }

    mysql_close(connexion);

    // Si on a rien ajouté → renvoyer NULL
    if (strlen(reponse) == 0) {
        // free(reponse);
        // return NULL;
        sprintf(reponse, "Pas de patients connecte");
    }

    return reponse;
}