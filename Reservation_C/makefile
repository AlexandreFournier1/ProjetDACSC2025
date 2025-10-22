.SILENT:
EXEC_INTERFACE = Consultationbooker
ECHO = Compilation de
DOSSIER = ClientConsultationBookerQt
LIB = lib

SOURCES = $(DOSSIER)/main.cpp $(DOSSIER)/mainwindowclientconsultationbooker.cpp $(DOSSIER)/moc_mainwindowclientconsultationbooker.cpp
OBJECTS = $(SOURCES:.cpp=.o)

SERVER_SOURCES = Server_Reservation.cpp $(LIB)/TCP.cpp $(LIB)/SCP.cpp 
SERVER_OBJECTS = $(SERVER_SOURCES:.cpp=.o)
SERVER_EXEC = Server_Reservation

CLIENT_SOURCES = Client_Reservation.cpp $(LIB)/TCP.cpp
CLIENT_OBJECTS = $(CLIENT_SOURCES:.cpp=.o)
CLIENT_EXEC = Client_Reservation

all: $(EXEC_INTERFACE) $(SERVER_EXEC) $(CLIENT_EXEC) $(LIB)/CBP.o


$(EXEC_INTERFACE): $(OBJECTS)
	echo $(ECHO) $(EXEC_INTERFACE)
	g++ $(OBJECTS) -o $(EXEC_INTERFACE) -lQt5Widgets -lQt5Gui -lQt5Core -lpthread

%.o: %.cpp
	echo $(ECHO) $@
	g++ -fPIC -c $< -o $@ -I/usr/include/qt5 -I/usr/include/qt5/QtWidgets -I/usr/include/qt5/QtGui -I/usr/include/qt5/QtCore

$(LIB)/TCP.o: $(LIB)/TCP.cpp
	echo $(ECHO) TCP.o
	g++ -Wall $(LIB)/TCP.cpp -c -o $(LIB)/TCP.o

$(LIB)/SCP.o: $(LIB)/SCP.cpp
	echo $(ECHO) SCP.o
	g++ -Wall $(LIB)/SCP.cpp -c -o $(LIB)/SCP.o

$(LIB)/CBP.o: $(LIB)/CBP.cpp
	echo $(ECHO) CBP.o
	g++ -Wall $(LIB)/CBP.cpp -c -o $(LIB)/CBP.o

$(SERVER_EXEC): $(SERVER_OBJECTS)
	echo $(ECHO) $(SERVER_EXEC)
	g++ -Wall $(SERVER_OBJECTS) -o $(SERVER_EXEC) -pthread

$(CLIENT_EXEC): $(CLIENT_OBJECTS)
	echo $(ECHO) $(CLIENT_EXEC)
	g++ -Wall $(CLIENT_OBJECTS) -o $(CLIENT_EXEC)

%.o: %.cpp
	g++ -Wall -Ilib -c $< -o $@

clean:
	echo Nettoyage des objets et des exécutables
	rm -f $(OBJECTS) $(SERVER_OBJECTS) $(LIB)/TCP.o $(EXEC_INTERFACE) $(SERVER_EXEC)

clobber: clean
	rm -f $(EXEC_INTERFACE) $(SERVER_EXEC)
