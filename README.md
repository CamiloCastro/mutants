# Mutantes

La clase `MainClass` contiene el método `boolean isMutant(String[] dna);` el cual dado una
secuencia de ADN logra determinar si esa secuencia pertenece o no a un mutante. Se puede usar
la función main para ejecutar un ejemplo de la función.

En la clase `MutantService` se encuentra la implementación para utilizar la función previamente
descrita se pueda usar directamente en AWS en una función lambda. Para esto, hay una tarea
en gradle que se lla `buildZip`, la cual crea un archivo zip ya listo para subir a AWS Lambada.

La función lambda debe estar conectada a DynamoDB y a API Gateway para que funcione como servicio.
Particularmente debe existir una tabla en Dynamo llamada `ML-Test` que tenga como 
hash key un registro llamado `counters` y dos atributos llamados `count_human` y
`count_mutant` que llevan la cuenta de los mutantes y humanos evaluados, por medio de un contador atómico.


# API
El API fue publicado en AWS y funciona de la siguiente manera

`POST https://zn68dldh71.execute-api.us-east-1.amazonaws.com/public/ml-test/mutant`

Con un body:

`{
"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}`

Este devuelve 200 si es mutante y si no devuelve 403 junto con un mensaje.


`GET https://zn68dldh71.execute-api.us-east-1.amazonaws.com/public/ml-test/stats`

Devuelve las estadísticas hasta ese momento de los mutantes y humanos.