
#extrai os arquivos dos serviços de backend

printf "Extraindo Elastic Search"

cd server/

tar -xf elasticsearch-5.2.0.tar

printf "\t\t\t[OK]\n"

#printf "Extraindo Kibana"

#tar -xf kibana-5.2.0-darwin-x86_64.tar

#printf "\t\t\t[OK]\n"

printf "Configurando Elastic "

cp -f elasticsearch.yml elasticsearch-5.2.0/config/elasticsearch.yml

printf "\t\t\t[OK]\n"

cd ..

#segue com a inicialização dos serviçois e indices
./init.sh
