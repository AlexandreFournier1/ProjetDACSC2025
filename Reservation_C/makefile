.SILENT:

FLAGS = -Ilib -I/usr/include/mysql -Wall -Wextra -std=c++17
LIB = lib/
EXEC_FOLDER = exec/

LIBS = -pthread /usr/lib64/mysql/libmysqlclient.so

$(EXEC_FOLDER)Server_Reservation: $(EXEC_FOLDER)Server_Reservation.o $(EXEC_FOLDER)TCP.o $(EXEC_FOLDER)CBP.o $(EXEC_FOLDER)ACBP.o
	g++ $(FLAGS) -o $(EXEC_FOLDER)Server_Reservation $(EXEC_FOLDER)Server_Reservation.o $(EXEC_FOLDER)TCP.o $(EXEC_FOLDER)CBP.o $(EXEC_FOLDER)ACBP.o $(LIBS)
	echo "Compilation de Server_Reservation terminée."

$(EXEC_FOLDER)Server_Reservation.o: Server_Reservation.cpp $(LIB)TCP.h $(LIB)CBP.h $(LIB)ACBP.h
	g++ $(FLAGS) -c Server_Reservation.cpp -o $(EXEC_FOLDER)Server_Reservation.o

$(EXEC_FOLDER)TCP.o: $(LIB)TCP.cpp $(LIB)TCP.h
	g++ $(FLAGS) -c $(LIB)TCP.cpp -o $(EXEC_FOLDER)TCP.o
	echo "Compilation de la librairie TCP"

$(EXEC_FOLDER)CBP.o: $(LIB)CBP.cpp $(LIB)CBP.h
	g++ $(FLAGS) -c $(LIB)CBP.cpp -o $(EXEC_FOLDER)CBP.o
	echo "Compilation de la librairie CBP"

$(EXEC_FOLDER)ACBP.o: $(LIB)ACBP.cpp $(LIB)ACBP.h
	g++ $(FLAGS) -c $(LIB)ACBP.cpp -o $(EXEC_FOLDER)ACBP.o
	echo "Compilation de la librairie ACBP"

clean:
	rm -f *.o $(EXEC_FOLDER)*.o $(EXEC_FOLDER)Server_Reservation
	echo "Nettoyage terminé."