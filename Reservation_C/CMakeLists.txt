cmake_minimum_required(VERSION 3.16)
project(ClientConsultationBooker LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# ---------- Qt5 ----------
find_package(Qt5 COMPONENTS Widgets REQUIRED)

# ---------- Includes ----------
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/ClientConsultationBookerQt
    ${CMAKE_CURRENT_SOURCE_DIR}/lib
    /usr/include/mysql
)

# ---------- Qt auto ----------
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC OFF)
set(CMAKE_AUTORCC ON)

# ---------- Consultationbooker ----------
add_executable(Consultationbooker
    ClientConsultationBookerQt/main.cpp
    ClientConsultationBookerQt/mainwindowclientconsultationbooker.cpp
    ClientConsultationBookerQt/mainwindowclientconsultationbooker.h
    lib/TCP.cpp
    lib/CBP.cpp
    lib/TCP.h
    lib/CBP.h
)

# Ajout ici du lien vers MySQL
target_link_libraries(Consultationbooker PRIVATE Qt5::Widgets pthread /usr/lib64/mysql/libmysqlclient.so)

# ---------- Serveur ----------
add_executable(Server_Reservation
    Server_Reservation.cpp
    lib/TCP.cpp
    lib/ACBP.cpp
    lib/CBP.cpp
    lib/TCP.h
    lib/ACBP.h
    lib/CBP.h
)
target_include_directories(Server_Reservation PRIVATE lib)
target_link_libraries(Server_Reservation PRIVATE pthread)
target_link_libraries(Server_Reservation PRIVATE pthread /usr/lib64/mysql/libmysqlclient.so)

# ---------- ServerAdminTest ----------
add_executable(ServerAdminTest
    ServerAdminTest.cpp
    lib/TCP.cpp
    lib/ACBP.cpp
    lib/CBP.cpp
    lib/TCP.h
    lib/ACBP.h
    lib/CBP.h
)
target_include_directories(ServerAdminTest PRIVATE lib)
target_link_libraries(ServerAdminTest PRIVATE pthread)
target_link_libraries(ServerAdminTest PRIVATE pthread /usr/lib64/mysql/libmysqlclient.so)

# ---------- ServerPatientsTest ----------
add_executable(ServerPatientsTest
    ServerPatientsTest.cpp
    lib/TCP.cpp
    lib/ACBP.cpp
    lib/CBP.cpp
    lib/TCP.h
    lib/ACBP.h
    lib/CBP.h
)
target_include_directories(ServerPatientsTest PRIVATE lib)
target_link_libraries(ServerPatientsTest PRIVATE pthread)
target_link_libraries(ServerPatientsTest PRIVATE pthread /usr/lib64/mysql/libmysqlclient.so)

# ---------- Output directory ----------
set_target_properties(Consultationbooker Server_Reservation ServerPatientsTest ServerAdminTest
    PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin
)

# ---------- Warnings ----------
if (MSVC)
    target_compile_options(Consultationbooker PRIVATE /W4)
else()
    target_compile_options(Consultationbooker PRIVATE -Wall -Wextra -Wpedantic)
endif()
