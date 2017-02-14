#!/bin/bash

export IFS=","

#Inicializa os serviços
./start_services.sh

#Configure index
./configure_index.sh

#Load hotel database
./load_hotel_database.sh

#Load availability database
./load_availability_database.sh

printf "Inicialização completa\n\n"
