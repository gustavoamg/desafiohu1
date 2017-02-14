#!/bin/bash

printf "Removendo dados antigos..."

curl -s -XDELETE "http://localhost:9200/hotel-urbano/" > /dev/null

printf "\t\t\t\t[OK]\n"

printf "Configurando indice..."

curl -s -XPUT "http://localhost:9200/hotel-urbano" -d'
{
  "mappings": {
    "disponibilidade": {
      "properties": {
        "data": {
          "type":   "date",
          "format": "d/MM/yyyy"
        },
        "hotel": {
          "type": "nested"
        }
      }
    }
  }
}' > /dev/null

printf "\t\t\t\t\t[OK]\n"
