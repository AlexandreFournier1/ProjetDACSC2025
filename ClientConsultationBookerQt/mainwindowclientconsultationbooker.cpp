#include "mainwindowclientconsultationbooker.h"
#include "ui_mainwindowclientconsultationbooker.h"
#include <QInputDialog>
#include <QMessageBox>
#include <iostream>
#include <unistd.h>

#include "../lib/TCP.h"
#include "../lib/CBP.h"

using namespace std;

void Echange(char* requete, char* reponse);

MainWindowClientConsultationBooker::MainWindowClientConsultationBooker(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindowClientConsultationBooker)
{
    ui->setupUi(this);
    logoutOk();

    // Configuration de la table des employes (Personnel Garage)
    ui->tableWidgetConsultations->setColumnCount(5);
    ui->tableWidgetConsultations->setRowCount(0);
    QStringList labelsTableConsultations;
    labelsTableConsultations << "Id" << "Spécialité" << "Médecin" << "Date" << "Heure";
    ui->tableWidgetConsultations->setHorizontalHeaderLabels(labelsTableConsultations);
    ui->tableWidgetConsultations->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetConsultations->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetConsultations->setEditTriggers(QAbstractItemView::NoEditTriggers);
    ui->tableWidgetConsultations->horizontalHeader()->setVisible(true);
    ui->tableWidgetConsultations->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetConsultations->verticalHeader()->setVisible(false);
    ui->tableWidgetConsultations->horizontalHeader()->setStyleSheet("background-color: lightyellow");
    int columnWidths[] = {40, 150, 200, 150, 100};
    for (int col = 0; col < 5; ++col)
        ui->tableWidgetConsultations->setColumnWidth(col, columnWidths[col]);

    // Exemples d'utilisation (à supprimer)
    // this->addTupleTableConsultations(1,"Neurologie","Martin Claire","2025-10-01", "09:00");
    // this->addTupleTableConsultations(2,"Cardiologie","Lemoine Bernard","2025-10-06", "10:15");
    // this->addTupleTableConsultations(3,"Dermatologie","Maboul Paul","2025-10-23", "14:30");

    //this->addComboBoxSpecialties("--- TOUTES ---");
    // this->addComboBoxSpecialties("Dermatologie");
    // this->addComboBoxSpecialties("Cardiologie");

    //this->addComboBoxDoctors("--- TOUS ---");
    // this->addComboBoxDoctors("Martin Claire");
    // this->addComboBoxDoctors("Maboul Paul");
}

MainWindowClientConsultationBooker::~MainWindowClientConsultationBooker()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table des livres encodés (ne pas modifier) ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::addTupleTableConsultations(int id,
                                                                    string specialty,
                                                                    string doctor,
                                                                    string date,
                                                                    string hour)
{
    int nb = ui->tableWidgetConsultations->rowCount();
    nb++;
    ui->tableWidgetConsultations->setRowCount(nb);
    ui->tableWidgetConsultations->setRowHeight(nb-1,10);

    // id
    QTableWidgetItem *item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(id));
    ui->tableWidgetConsultations->setItem(nb-1,0,item);

    // specialty
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(specialty));
    ui->tableWidgetConsultations->setItem(nb-1,1,item);

    // doctor
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(doctor));
    ui->tableWidgetConsultations->setItem(nb-1,2,item);

    // date
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(date));
    ui->tableWidgetConsultations->setItem(nb-1,3,item);

    // hour
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(hour));
    ui->tableWidgetConsultations->setItem(nb-1,4,item);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::clearTableConsultations() {
    ui->tableWidgetConsultations->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientConsultationBooker::getSelectionIndexTableConsultations() const
{
    QModelIndexList list = ui->tableWidgetConsultations->selectionModel()->selectedRows();
    if (list.size() == 0) return -1;
    QModelIndex index = list.at(0);
    int ind = index.row();
    return ind;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles des comboboxes (ne pas modifier) //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::addComboBoxSpecialties(string specialty) {
    ui->comboBoxSpecialties->addItem(QString::fromStdString(specialty));
}

string MainWindowClientConsultationBooker::getSelectionSpecialty() const {
    return ui->comboBoxSpecialties->currentText().toStdString();
}

void MainWindowClientConsultationBooker::clearComboBoxSpecialties() {
    ui->comboBoxSpecialties->clear();
    this->addComboBoxSpecialties("--- TOUTES ---");
}

void MainWindowClientConsultationBooker::addComboBoxDoctors(string doctor) {
    ui->comboBoxDoctors->addItem(QString::fromStdString(doctor));
}

string MainWindowClientConsultationBooker::getSelectionDoctor() const {
    return ui->comboBoxDoctors->currentText().toStdString();
}

void MainWindowClientConsultationBooker::clearComboBoxDoctors() {
    ui->comboBoxDoctors->clear();
    this->addComboBoxDoctors("--- TOUS ---");
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonction utiles de la fenêtre (ne pas modifier) ////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientConsultationBooker::getLastName() const {
    return ui->lineEditLastName->text().toStdString();
}

string MainWindowClientConsultationBooker::getFirstName() const {
    return ui->lineEditFirstName->text().toStdString();
}

int MainWindowClientConsultationBooker::getPatientId() const {
    return ui->spinBoxId->value();
}

void MainWindowClientConsultationBooker::setLastName(string value) {
    ui->lineEditLastName->setText(QString::fromStdString(value));
}

string MainWindowClientConsultationBooker::getStartDate() const {
    return ui->dateEditStartDate->date().toString("yyyy-MM-dd").toStdString();
}

string MainWindowClientConsultationBooker::getEndDate() const {
    return ui->dateEditEndDate->date().toString("yyyy-MM-dd").toStdString();
}

void MainWindowClientConsultationBooker::setFirstName(string value) {
    ui->lineEditFirstName->setText(QString::fromStdString(value));
}

void MainWindowClientConsultationBooker::setPatientId(int value) {
    if (value > 0) ui->spinBoxId->setValue(value);
}

bool MainWindowClientConsultationBooker::isNewPatientSelected() const {
    return ui->checkBoxNewPatient->isChecked();
}

void MainWindowClientConsultationBooker::setNewPatientChecked(bool state) {
    ui->checkBoxNewPatient->setChecked(state);
}

void MainWindowClientConsultationBooker::setStartDate(string date) {
    QDate qdate = QDate::fromString(QString::fromStdString(date), "yyyy-MM-dd");
    if (qdate.isValid()) ui->dateEditStartDate->setDate(qdate);
}

void MainWindowClientConsultationBooker::setEndDate(string date) {
    QDate qdate = QDate::fromString(QString::fromStdString(date), "yyyy-MM-dd");
    if (qdate.isValid()) ui->dateEditEndDate->setDate(qdate);
}

void MainWindowClientConsultationBooker::loginOk() {
    ui->lineEditLastName->setReadOnly(true);
    ui->lineEditFirstName->setReadOnly(true);
    ui->spinBoxId->setReadOnly(true);
    ui->checkBoxNewPatient->setEnabled(false);
    ui->pushButtonLogout->setEnabled(true);
    ui->pushButtonLogin->setEnabled(false);
    ui->pushButtonRechercher->setEnabled(true);
    ui->pushButtonReserver->setEnabled(true);
}

void MainWindowClientConsultationBooker::logoutOk() {
    ui->lineEditLastName->setReadOnly(false);
    setLastName("");
    ui->lineEditFirstName->setReadOnly(false);
    setFirstName("");
    ui->spinBoxId->setReadOnly(false);
    setPatientId(1);
    ui->checkBoxNewPatient->setEnabled(true);
    setNewPatientChecked(false);
    ui->pushButtonLogout->setEnabled(false);
    ui->pushButtonLogin->setEnabled(true);
    ui->pushButtonRechercher->setEnabled(false);
    ui->pushButtonReserver->setEnabled(false);
    setStartDate("2025-09-15");
    setEndDate("2025-12-31");
    clearComboBoxDoctors();
    clearComboBoxSpecialties();
    clearTableConsultations();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier) ///////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::dialogMessage(const string& title,const string& message) {
   QMessageBox::information(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::dialogError(const string& title,const string& message) {
   QMessageBox::critical(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientConsultationBooker::dialogInputText(const string& title,const string& question) {
    return QInputDialog::getText(this,QString::fromStdString(title),QString::fromStdString(question)).toStdString();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientConsultationBooker::dialogInputInt(const string& title,const string& question) {
    return QInputDialog::getInt(this,QString::fromStdString(title),QString::fromStdString(question));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions gestion des boutons (TO DO) //////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int sClient;

void MainWindowClientConsultationBooker::on_pushButtonLogin_clicked()
{
    //char ip[] = "192.168.150.131";
    char ip[] = "0.0.0.0";
    char* requete = (char*)malloc(MAX_SIZE_REQUETE), *reponse = (char*)malloc(MAX_SIZE_REPONSE);

    // Connexion sur le serveur
    if ((sClient = ClientSocket( ip, 50000)) == -1)
    {
        perror("Erreur de ClientSocket");
        exit(1);
    }
    printf("Connecte sur le serveur.\n");

    string lastName = this->getLastName();
    string firstName = this->getFirstName();
    int patientId = this->getPatientId();
    bool newPatient = this->isNewPatientSelected();

    if(lastName.empty()) return;
    if(firstName.empty()) return;
    if(patientId == 0) return;
    //if(!newPatient) return;

    cout << "lastName = " << lastName << endl;
    cout << "FirstName = " << firstName << endl;
    cout << "patientId = " << patientId << endl;
    cout << "newPatient = " << newPatient << endl;

    if (newPatient)
        patientId = -1;

    sprintf(requete, "LOGIN#%s#%s#%d", lastName.c_str(), firstName.c_str(), patientId);

    Echange(requete, reponse);

    char *ptr = strtok(reponse, "#");

    if (ptr && strcmp(ptr, "LOGIN") == 0)
    {
        char *status = strtok(NULL, "#");
        if (status && strcmp(status, "ok") == 0)
        {
            dialogMessage("Success", "Login réussi !");
            loginOk();

            specialitiesInitialisation();
            doctorsInitialisation();
            clearTableConsultations();
        }
        else
        {
            char *msg = strtok(NULL, "#");
            if (msg && strcmp(msg, "Client déjà loggé !") == 0)
                dialogError("Erreur", "Client déjà loggé !");
            else
                dialogError("Erreur", "Identifiants invalides !");
        }
    }
    
    free(requete);
    free(reponse);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void MainWindowClientConsultationBooker::on_pushButtonLogout_clicked()
{
    char* requete = (char*)malloc(MAX_SIZE_REQUETE), *reponse = (char*)malloc(MAX_SIZE_REPONSE);

    string lastName = this->getLastName();
    string firstName = this->getFirstName();
    int patientId = this->getPatientId();

    sprintf(requete, "LOGOUT#%s#%s#%d", lastName.c_str(), firstName.c_str(), patientId);

    Echange(requete, reponse);

    char *ptr = strtok(requete, "#");

    if (strcmp(strtok(NULL, "#"), "ok"))
        logoutOk();

    clearComboBoxDoctors();

    free(requete);
    free(reponse);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void MainWindowClientConsultationBooker::on_pushButtonRechercher_clicked()
{
    char* requete = (char*)malloc(MAX_SIZE_REQUETE), *reponse = (char*)malloc(MAX_SIZE_REPONSE);

    string specialty = this->getSelectionSpecialty();
    string doctor = this->getSelectionDoctor();
    string startDate = this->getStartDate();
    string endDate = this->getEndDate();

    if(specialty.empty()) return;
    if(doctor.empty()) return;
    if(startDate.empty()) return;
    if(endDate.empty()) return;

    cout << "specialty = " << specialty << endl;
    cout << "doctor = " << doctor << endl;
    cout << "startDate = " << startDate << endl;
    cout << "endDate = " << endDate << endl;

    sprintf(requete, "SEARCHCONSULTATIONS#%s#%s#%s#%s", specialty.c_str(), doctor.c_str(), startDate.c_str(), endDate.c_str());

    Echange(requete, reponse);

    char *saveptr1, *saveptr2;

    char *ligne = strtok_r(reponse, "#", &saveptr1);

    char *suivant = strtok_r(NULL, "#", &saveptr1);

    if (strcmp(suivant, "Pas de Consultations trouvees !") == 0)
    {
        dialogError("Erreur", "Pas de Consultations trouvées !");
        clearTableConsultations();
    }
    else
    {
        dialogMessage("Success", "Consultations trouvées !");

        clearTableConsultations();

        ligne = strtok_r(NULL, "#", &saveptr1); // passer au premier vrai enregistrement

        while (ligne != NULL)
        {
            if (strlen(ligne) > 0)
            {
                char *id = strtok_r(ligne, ";", &saveptr2);  
                char *lastNameMedecin = strtok_r(NULL, ";", &saveptr2);
                char *firstNameMedecin = strtok_r(NULL, ";", &saveptr2);
                char *nameSpeciality = strtok_r(NULL, ";", &saveptr2);
                char *date = strtok_r(NULL, ";", &saveptr2);
                char *hour = strtok_r(NULL, ";", &saveptr2);

                char nomComplet[100];
                sprintf(nomComplet, "%s %s", firstNameMedecin, lastNameMedecin);

                addTupleTableConsultations(atoi(id), nameSpeciality, nomComplet, date, hour);
            }

            ligne = strtok_r(NULL, "#", &saveptr1);
        }
    }

    free(requete);
    free(reponse);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void MainWindowClientConsultationBooker::on_pushButtonReserver_clicked()
{
    char* requete = (char*)malloc(MAX_SIZE_REQUETE), *reponse = (char*)malloc(MAX_SIZE_REPONSE);

    int selectedID = this->getSelectedConsultationId();
    string nom = this->getLastName();
    string prenom = this->getFirstName();
    string reason = dialogInputText("Raison", "Quel est votre raison ?");

    cout << "selectedID= " << selectedID << endl;
    

    if(selectedID == -1) return;

    sprintf(requete, "BOOKCONSULTATION#%d#%s#%s#%s", selectedID, nom.c_str(), prenom.c_str(), reason.c_str());

    printf("Avant : Echange\n");

    Echange(requete, reponse);

    printf("Après : Echange\n");

    char *ptr = strtok(reponse, "#");
    char *status = strtok(NULL, "#");

    if (ptr && status && strcmp(ptr, "BOOKCONSULTATION") == 0)
    {
        if (strcmp(status, "ok") == 0)
            dialogMessage("Succès", "Réservation effectuée avec succès !");
        else
            dialogError("Erreur", "Réponse inconnue du serveur !");
    }
    else
    {
        dialogError("Erreur", "Réponse invalide du serveur !");
    }

    cout << "selectedRow = " << selectedID << endl;

    free(requete);
    free(reponse);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
///////////////// Fonctions supplémentaires //////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

//***** Echange de données entre client et serveur ******************
void Echange(char* requete, char* reponse)
{
    int nbEcrits, nbLus;

    // Envoi de la requete
    if ((nbEcrits = Send(sClient, requete, strlen(requete))) == -1)
    {
        perror("Erreur de Send");
        close(sClient);
        exit(1);
    }

    // Attente de la reponse
    if ((nbLus = Receive(sClient, reponse)) < 0)
    {
        perror("Erreur de Receive");
        close(sClient);
        exit(1);
    }

    if (nbLus == 0)
    {
        printf("Serveur arrete, pas de reponse reçue...\n");
        close(sClient);
        exit(1);
    }

    reponse[nbLus] = 0;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void MainWindowClientConsultationBooker::doctorsInitialisation()
{
    char* req = (char*)malloc(MAX_SIZE_REQUETE), *rep = (char*)malloc(MAX_SIZE_REPONSE);

    sprintf(req, "GETDOCTORS");
    Echange(req, rep);

    // GETDOCTORS#1;Nom;Prenom#2;Nom;Prenom#3;Dubois;Lea#
    char *saveptr1, *saveptr2;

    char* ptr = strtok_r(rep, "#", &saveptr1);  

    if (ptr != NULL && strcmp(ptr, "GETDOCTORS") == 0)
    {   
        char *ligne = strtok_r(NULL, "#", &saveptr1);  
        int count = 0;
        while (ligne != NULL)
        {
            if (strlen(ligne) > 0)
            {
                char *id = strtok_r(ligne, ";", &saveptr2);  
                char *lastName = strtok_r(NULL, ";", &saveptr2);
                char *firstName = strtok_r(NULL, ";", &saveptr2);
                if (id && lastName && firstName)
                {
                    string doc = string(lastName) + " " + string(firstName);
                    addComboBoxDoctors(doc);
                    count++;
                }
            }
            ligne = strtok_r(NULL, "#", &saveptr1);
        }
    }

    free(req);
    free(rep);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void MainWindowClientConsultationBooker::specialitiesInitialisation()
{
    char* req = (char*)malloc(MAX_SIZE_REQUETE), *rep = (char*)malloc(MAX_SIZE_REPONSE);

    sprintf(req, "GETSPECIALITES");
    Echange(req, rep);

    // GETSPECIALITES#1;Cardiologie

    char *saveptr1, *saveptr2;

    char* ptr = strtok_r(rep, "#", &saveptr1);  

    if (ptr != NULL && strcmp(ptr, "GETSPECIALITES") == 0)
    {   
        char *ligne = strtok_r(NULL, "#", &saveptr1);  
        int count = 0;
        while (ligne != NULL)
        {
            if (strlen(ligne) > 0)
            {
                char *id = strtok_r(ligne, ";", &saveptr2);  
                char *specialty = strtok_r(NULL, ";", &saveptr2);
                if (id && specialty)
                {
                    addComboBoxSpecialties(specialty);
                    count++;
                }
            }
            ligne = strtok_r(NULL, "#", &saveptr1);
        }
    }

    free(req);
    free(rep);
}

int MainWindowClientConsultationBooker::getSelectedConsultationId()
{
    int row = getSelectionIndexTableConsultations();
    if (row == -1) return -1;

    QTableWidgetItem *item = ui->tableWidgetConsultations->item(row, 0);
    if (!item) return -1;

    return item->text().toInt();
}