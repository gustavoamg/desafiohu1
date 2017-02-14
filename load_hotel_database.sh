#!/bin/bash

export IFS=","

printf "Inicializando base de hoteis..."

cat artefatos/hoteis.txt | while read a b c; do

# echo "$a:$b:$c";

        curl -s -XPOST "http://localhost:9200/hotel-urbano/hotel/"$a -d'
        {
         "cidade": "'$b'",
         "nome": "'$c'"
        }' > /dev/null;

        printf "."

done

printf "\t[OK]\n"
