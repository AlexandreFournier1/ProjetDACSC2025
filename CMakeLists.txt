cmake_minimum_required(VERSION 3.16)
project(ClientConsultationBooker LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# ---------- Qt5 uniquement ----------
find_package(Qt5 COMPONENTS Widgets REQUIRED)

# ---------- Dossiers d'inclusion ----------
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/ClientConsultationBookerQt
    ${CMAKE_CURRENT_SOURCE_DIR}/lib
    /usr/include/mysql  # Ajout des headers MySQL
)

# ---------- Qt automatique ----------
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC OFF)
set(CMAKE_AUTORCC ON)

# ---------- Fichiers sources pour l'interface graphique ----------
set(UI_SOURCES
    ClientConsultationBookerQt/main.cpp
    ClientConsultationBookerQt/mainwindowclientconsultationbooker.cpp
)

# ---------- Fichiers headers ----------
set(UI_HEADERS
    ClientConsultationBookerQt/mainwindowclientconsultationbooker.h
)

# ---------- Fichiers lib ----------
set(LIB_SOURCES
    lib/TCP.cpp
    lib/SCP.cpp
    lib/CBP.cpp
)

set(LIB_HEADERS
    lib/TCP.h
    lib/SCP.h
    lib/CBP.h
)

# ---------- Exécutable Consultationbooker ----------
add_executable(Consultationbooker
    ${UI_SOURCES}
    ${UI_HEADERS}
    ${LIB_SOURCES}
    ${LIB_HEADERS}
)

target_link_libraries(Consultationbooker PRIVATE Qt5::Widgets pthread)

# ---------- Exécutable serveur ----------
add_executable(Server_Reservation
    Server_Reservation.cpp
    lib/TCP.cpp
    lib/SCP.cpp
    lib/TCP.h
    lib/SCP.h
)

target_include_directories(Server_Reservation PRIVATE lib)
target_link_libraries(Server_Reservation PRIVATE pthread)

# ---------- Exécutable client ----------
add_executable(Client_Reservation
    Client_Reservation.cpp
    lib/TCP.cpp
    lib/CBP.cpp
    lib/TCP.h
    lib/CBP.h
)

target_include_directories(Client_Reservation PRIVATE lib)
target_link_libraries(Client_Reservation PRIVATE pthread /usr/lib64/mysql/libmysqlclient.so)

# ---------- Emplacement des exécutables ----------
set_target_properties(Consultationbooker Server_Reservation Client_Reservation
    PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin
)

# ---------- Options de compilation ----------
if (MSVC)
    target_compile_options(Consultationbooker PRIVATE /W4)
else()
    target_compile_options(Consultationbooker PRIVATE -Wall -Wextra -Wpedantic)
endif()
