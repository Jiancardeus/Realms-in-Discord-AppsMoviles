# Realms-in-Discord-
 1- Nombre : (Realms in Discord) Este nace de la idea impulsiva de crear un TCG mas centrado en la estrategia que en el RNG o la coleccion de cartas caras/meta.
    El nombre se crea directamente del mundo del juego, donde los Reinos entran en Discordia, literalmente.

 2- Integrantes: Isaac Rivas / Jianco Rivera

 3- Funcionalidades : CRUD PRINCIPAL = Principalmente tiene un CRUD a traves de Spring BOOT hacia una base de datos mediante una API/REST. Funciones de Login y Register, Actualizar nombre de usuario y eliminar cuenta mediante mongoId.
                      Biblioteca = Nos permite acceder a una biblioteca guardada en nuestra base de datos mediante una API/REST. Funciones de GET/READ, Tenemos backup local en caso de fallo de la API para que el usuario final vea cartas sin importar los fallos.
                      Mazos (DECK_BUILDER) = Nos permite crear mazos, un conjunto de cartas a una lista mediante unas llamadas a la API donde guardamos las cartas, las importa directamnete desde la base de datos y en caso de fallo tenemos backup local para que de igual forma el usuario final vea cartas. Permite Guardar estas listas.
                      
 4- Endpoints utiliados =   http://10.0.2.2:5000 NodeJs funciones generales 
                            http://10.0.2.2:5001 API/REST CRUD completo
                              
 5.1- Exlpicacion directorios (DEMO/SERVER) = Demo es nuestro microservicio spring boot y server es nuestro backend general.
 5.2- Paso 1. Abrir main (com.example.realmsindiscord) carpeta demo y carpeta server.
    Paso 2. Abrir terminales en demo y server
    Paso 3. Ejecutar Npm install mongodb y npm install en Server y en Demo ./mvnw clean install
    Paso 4. Ejecturar Npm start en server y en Demo ./mvnw spring-boot:run
    Paso 5. Disfrutar
 6- 
 7- Tablero de trello = https://trello.com/b/2iKBfymw/mobil