#!/bin/bash

printf "Iniciando Elastic Search Engine "

#Inicia serviço elastic search
server/elasticsearch-5.2.0/bin/elasticsearch > log/elastic.log 2>&1 &

#verifica status da inicialização
elastic_initialized=`grep started log/elastic.log | wc -l`

while [ ! $elastic_initialized -gt 0 ]; do
        printf "."
        sleep 5

        elastic_initialized=`grep started log/elastic.log | wc -l`
done

printf  "\t\t\t[OK]\n"

printf "Inicializando Kibana "

#inicializa o processo do kibana
server/kibana-5.2.0-darwin-x86_64/bin/kibana > log/kibana.log 2>&1 &

#verifica status da inicialização
kibana_initialized=`grep "Status changed from yellow to green - Ready" log/kibana.log | wc -l`

while [ ! $kibana_initialized -gt 0 ]; do
        printf "."
        sleep 5

        kibana_initialized=`grep "Status changed from yellow to green - Ready" log/kibana.log | wc -l`
done

printf  "\t\t\t\t[OK]\n"
