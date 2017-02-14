#!/bin/bash

export IFS=","

printf "Inicializando disponibilidade"

cat artefatos/disp.txt | while read a b c d; do

# echo "$a:$b:$c:$d";

        hotelObject=`curl -s -XGET "http://localhost:9200/hotel-urbano/hotel/$a/_source"`

#echo $hotelObject

        curl -s -XPOST "http://localhost:9200/hotel-urbano/disponibilidade/" -d'
        {
         "hotel": '"$hotelObject"',
         "data": "'$b'",
         "disponibilidadde": "'$c'",
         "minimo_de_noites" : "'$d'"
        }' > /dev/null;

        printf "."
done

printf "\t[OK]\n"
