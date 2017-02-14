#!/bin/bash

#kibanaPID=`ps -ef | grep kibana | grep -v grep | awk '{print $2}'`

elasticPID=`ps -ef | grep elastic | grep -v grep | awk '{print $2}'`

#printf "Encerrando Kibana - PID[$kibanaPID]"

#kill $kibanaPID

#printf "\t\t\t[OK]\n"

printf "Encerrando Eslastic - PID [$elasticPID]"

kill $elasticPID

printf "\t\t[OK]\n"

printf "Serviços paralizados\n\n"
