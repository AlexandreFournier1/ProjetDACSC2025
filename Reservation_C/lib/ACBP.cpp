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
		char* req[512], rep[4096];

		strcpy(req, "GETPATIENTCONNECTE");

		CBP(req, rep, socket);

		sprintf(reponse, "%s", rep);
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* ACBP_LIST_CLIENTS()
{

}