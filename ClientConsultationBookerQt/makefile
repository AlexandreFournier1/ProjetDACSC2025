.SILENT:
EXEC=consultationbooker
ECHO=Compilation de

SOURCES=main.cpp mainwindowclientconsultationbooker.cpp moc_mainwindowclientconsultationbooker.cpp
OBJECTS=$(SOURCES:.cpp=.o)

all: $(EXEC)

$(EXEC): $(OBJECTS)
	echo $(ECHO) $(EXEC)
	g++ $(OBJECTS) -o $(EXEC) -lQt5Widgets -lQt5Gui -lQt5Core -lpthread

%.o: %.cpp
	echo $(ECHO) $@
	g++ -fPIC -c $< -o $@ -I/usr/include/qt5 -I/usr/include/qt5/QtWidgets -I/usr/include/qt5/QtGui -I/usr/include/qt5/QtCore


clean:
	echo Nettoyage des objets et de l'exécutable
	rm -f $(OBJECTS) $(EXEC)
