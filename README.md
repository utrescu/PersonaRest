Servei REST de persones
===========================

Es tracta d'un servei REST senzill simplement pensat per provar si es pot fer servir SparkJava des de Kotlin. 
La única cosa que fa és permetre visualitzar

Testing Kotlin and SparkJava to create a simple REST service

Executar
===========================
He fet el projecte amb Maven (el proper cop ho faré amb Gradle...) i per tant si tenim Java i Maven 
en el sistema primer hem de compilar el programa:

    $ mvn package

I després ja es podrà executar: 

    $ java -jar target/RestPersona-1.0-SNAPSHOT-jar-with-dependencies.jar
    
Ho sé, ho sé, el nom és un pèl llarg :-P 

Es pot anar amb el navegador a la pàgina http://localhost:4567/ i es mostrarà l'ajuda (que també es pot 
veure aquí sota) i s'hi podran provar els mètodes GET

Mètodes
=========================

Obtenir una persona a partir de l'id
---------------------------------------

Per obtenir una persona a partir del seu codi es fa servir la URL següent:

> GET http://localhost:4567/persones/{codi}

Per exemple per obtenir la persona amb el codi '1' enviem (faig servir httpie)

    $ http GET http://localhost:4567/persones/1
    HTTP/1.1 200 OK
    Content-Type: application/json
    Date: Thu, 01 Jun 2017 20:36:56 GMT
    Server: Jetty(9.4.z-SNAPSHOT)
    Transfer-Encoding: chunked

    {
        "cognom": "Sala",
        "id": 1,
        "nom": "Xavier"
    }

Si es demana un id que no existeix el resultat és un error:

      $ http localhost:4567/persones/123456
      HTTP/1.1 404 Not Found
      Content-Type: application/json
      Date: Thu, 01 Jun 2017 20:45:33 GMT
      Server: Jetty(9.4.z-SNAPSHOT)
      Transfer-Encoding: chunked

      {"error"="No existeix"}

Obtenir les persones que tenen el mateix nom
-------------------------------------------------

Es pot obtenir una llista amb les persones emmagatzemades en el sistema que tenen el mateix nom fent una petició GET a la URL

> GET http://localhost:4567/persones/nom/{nom}

O sigui per obtenir totes les persones del sistema que es diuen Joan:

    $ http GET localhost:4567/persones/nom/Joan
    HTTP/1.1 200 OK
    Content-Type: application/json
    Date: Thu, 01 Jun 2017 20:52:39 GMT
    Server: Jetty(9.4.z-SNAPSHOT)
    Transfer-Encoding: chunked

    [
        {
            "cognom": "Perez",
            "id": 4,
            "nom": "Joan"
        },
        {
            "cognom": "Pi",
            "id": 6,
            "nom": "Joan"
        }
    ]

En cas de que no n'hi hagi cap el resultat serà un array buit

  []

Afegir una persona al sistema
---------------------------------

Hi ha dues formes d'afegir una persona al sistema:

1. A través de paràmetres
2. Enviant un JSON en el cos del missatge

### Crear una persona amb paràmetres

S'envia un POST amb els paràmetres 'nom' i 'cognom'

> POST /persones/?nom={nom}&cognom={cognom}

Ens retornarà un JSON amb la persona creada

    $ http POST localhost:4567/persones/ nom=="Pep" cognom=="Punyetes"
    HTTP/1.1 201 Created
    Content-Type: application/json
    Date: Thu, 01 Jun 2017 21:01:34 GMT
    Server: Jetty(9.4.z-SNAPSHOT)
    Transfer-Encoding: chunked

    {
        "cognom": "Punyetes",
        "id": 8,
        "nom": "Pep"
    }

### Crear una persona amb paràmetres

Es pot enviar el nom i el cognom en el cos del missatge en format POST

> POST /persones/add
>
> {
>    "cognom": "Ferro",
>     "nom": "Pep"
> }

Eliminar una persona
-------------------------

Si s'envia un missatge amb DELETE s'elimina la persona amb l'id especificat

> DELETE http://localhost:4567/persones/{id}

O sigui si volem eliminar la persona amb l'id número '2':

    $ http DELETE localhost:4567/persones/2

    HTTP/1.1 204 No Content
    Content-Type: application/json
    Date: Thu, 01 Jun 2017 21:07:52 GMT
    Server: Jetty(9.4.z-SNAPSHOT)


