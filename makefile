.SILENT:
EXEC_INTERFACE=consultationbooker
ECHO=Compilation de
DOSSIER=ClientConsultationBookerQt
LIB=lib

SOURCES=$(DOSSIER)/main.cpp $(DOSSIER)/mainwindowclientconsultationbooker.cpp $(DOSSIER)/moc_mainwindowclientconsultationbooker.cpp
OBJECTS=$(SOURCES:.cpp=.o)

all: $(EXEC_INTERFACE) $(LIB)/TCP.o

$(EXEC_INTERFACE): $(OBJECTS)
	echo $(ECHO) $(EXEC_INTERFACE)
	g++ $(OBJECTS) -o $(EXEC_INTERFACE) -lQt5Widgets -lQt5Gui -lQt5Core -lpthread

%.o: %.cpp
	echo $(ECHO) $@
	g++ -fPIC -c $< -o $@ -I/usr/include/qt5 -I/usr/include/qt5/QtWidgets -I/usr/include/qt5/QtGui -I/usr/include/qt5/QtCore

$(LIB)/TCP.o: $(LIB)/TCP.cpp
		g++ -Wall $(LIB)/TCP.cpp -c -o $(LIB)/TCP.o


clean:
	echo Nettoyage des objets et de lexécutable
	rm -f $(OBJECTS) 

clobber:	clean
			rm-f $(EXEC_INTERFACE)
