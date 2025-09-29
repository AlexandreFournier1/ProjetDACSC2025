#include "CBP.h"

// Pas encore tester du coup je sais pas si ça fonctionne 
char* CBP_Login(char* nom, char* prenom, int numPatient) // numPatient = -1 si nouveau Patient
{
	// Connection à la BD
  	MYSQL* connexion = mysql_init(NULL);
  	mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0);

  	char requete[255];

  	if (numPatient == -1)
  	{
  		sprintf(requete,"insert into patients (last_name, first_name) values ('%s', '%s')", nom, prenom);
	  	mysql_query(connexion,requete);
  	}

  	sprintf(requete,"select id from patients where last_name = '%s' and first_name = '%s' and id = '%d'", nom, prenom, numPatient);
  	mysql_query(connexion,requete);

	MYSQL_RES* resultat = mysql_store_result(connexion);
	if (resultat)
		if (numPatient == -1)
			sprintf(requete, "LOGIN#Oui#%s", resultat);
		else
			sprintf(requete, "LOGIN#Oui");
		
	else
		sprintf(requete, "LOGIN#Non");

	// Deconnection de la BD
	mysql_close(connexion);

	return requete;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

char* CBP_Logout()
{
	return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

Specialite* CBP_GetSpecialites()
{
	return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

Docteur* CBP_GetDoctors()
{
	return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

Consultation* CBP_SearchConsultations(Specialite specialite, Docteur medecin, char* dateDebut, char* dateFin)
{
	return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////

bool CBP_BookConsultation(int consulationId, char* reason)
{
	return 0;
}