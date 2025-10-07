# Nom de l'exécutable
DB_CREATOR = create_db

# Fichiers sources
SRC = CreationBD.cpp

# Options de compilation
CXX = g++
CXXFLAGS = -Wall -Wextra -std=c++17 -I/usr/include/mysql

# Librairies à linker (chemin vers le dossier contenant libmysqlclient.so)
LIBS = -L/usr/lib64/mysql -lmysqlclient

# Règle par défaut : tout
all: $(DB_CREATOR)

# Compilation de l'exécutable
$(DB_CREATOR): $(SRC)
	$(CXX) $(CXXFLAGS) -o $(DB_CREATOR) $(SRC) $(LIBS)

# Nettoyage
clean:
	rm -f $(DB_CREATOR)
