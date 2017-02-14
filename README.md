# Desafio de auto-complete e busca disponibilidade

Neste problema você deve implementar o widget de busca de hoteis. Este desenvolvimento engloba o auto-complete de hoteis e a busca por disponibilidades quando o usuário informa um periodo de estadia. 

A interface em anexo precisa ser implementada assim como o backend para consumir a lista de hoteis e as disponibilidades. Tudo será avaliado. Faça o seu melhor na linguagem onde vc possui o maior domínio.

***Restrições***
* Eu preciso conseguir rodar seu código no mac os x OU no ubuntu;
* Eu vou executar seu código com os seguintes comandos:

>1. *git clone seu-fork*
2. *cd seu-fork*
3. *comando para instalar dependências*
4. *comando para executar a aplicação*

Esses comandos tem que ser o suficiente para configurar meu mac os x OU ubuntu e rodar seu programa. Pode considerar que eu tenho instalado no meu sistema Python, Java, PHP, Ruby, Android, iOS e/ou Node. Qualquer outra dependência que eu precisar vc tem que prover.

***Performance***
* Preciso que os seus serviços suportem um volume de 1000 requisições por segundo

***Artefatos***
* Imagens e database de hoteis e disponibilidades estão na pasta artefatos

****************************************************************************************

* Instruções

1- Execute o comando setup.sh;

2- Instale o apk do aplicativo (desafiohu1/dev/DesafioHotelUrbano/app/build/outputs/apk/app-debug.apk) em um aparelho Android;

3- Ao executar o aplicativo, será solicitado que informe o IP do servidor backend. Forneça o endereço correto e clique em ENVIAR.

* DEPENDENCIAS

A única dependencia externa é o Java.
Para o aplicativo se comunicar com o Backend, é preciso que a porta 9200 do servidor esteja aberta para requisições.

* Referencia dos Comandos:

- setup.sh -> Extrai os serviços de backend e faz a inicialização dos dados
- init.sh -> Levanta os serviços de backend e faz a carga de dados inicial.
- configure_index.sh -> Apaga todos os dados existentes e recria a estrutura do indice
- load_hotel_database.sh -> Carrega a base de dados de Hoteis
- load_availability_database.sh -> Carrega a base de dados de Disponibilidade
- reload_data.sh -> Apaga todos os registros anteriores e refaz a carga de dados inicial
- start_services.sh -> Inicializa os serviços de backend, sem alterar os dados
- stop_services.sh -> Interrompe os serviços de backend
- cleanup.sh -> Remove as instalações do Elastic e do Kibana
