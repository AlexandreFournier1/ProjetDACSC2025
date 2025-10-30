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

	// LIST_CLIENTS
	if (strcmp(ptr, "LIST_CLIENTS") == 0)
	{
		char* temp = (char*)malloc(4096);
		char* rep;

		temp = getPatientConnecte();

		rep = ACBP_LIST_CLIENTS(temp);

		sprintf(reponse, "%s", rep);

		free(temp);
	}

	return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* ACBP_LIST_CLIENTS(char* patients)
{
	if (strcmp(patients, "") == 0 || strcmp(patients, NULL) == 0)
		return NULL;

	char* reponse = NULL;

	char tmp[4096];
	strcpy(tmp, patients);

	printf("Connexion a la BDD...\n");

	MYSQL* connexion = mysql_init(NULL);
	mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0);

	char *saveptr1, *saveptr2;

    char *ligne = strtok_r(tmp, "#", &saveptr1);

    while (ligne != NULL)
    {
    	if (strlen(ligne) > 0)
        {
            char *socket = strtok_r(ligne, ";", &saveptr2);  
            char *id = strtok_r(NULL, ";", &saveptr2);

            char requete[MAX_SIZE_REQUETE];

            sprintf(requete, "SELECT last_name, first_name FROM patients WHERE id = %s;", id);

            mysql_query(connexion,requete);

            MYSQL_RES* resultat = mysql_store_result(connexion);

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

        ligne = strtok_r(NULL, "#", &saveptr1);
    }

    mysql_close(connexion);

    return reponse;
}