# SmartStorage

AI powered document management application.

## TechStack

Java 25.0.2
Postgres 
SpringAI   
MinIO   
GraphQL   
Docker
RabbitMQ

## Description

The application is able to automatically mine user defined data from uploaded documents. 
User defines **Storage Section** with specified data the user wants to extract from document. 
This is the only thing the user needs to do before he can start "throwing" documents at the application.
After user uploads a file applications saves the file in its original form into MinIO storage. 
Right after that the application will fill out **Storage Item** object with the data extracted from the document.

Uploaded files are linked to **Storege Item** via **Storage Item Attachment**. the Storage Item can hold multiple files.
This is possible because when creating a new Storage Section, user defines which of queried fields are unique.
Based on this information the application detects common Storage Item between multiple uploaded documents.

for example: a document about car insurance and a document about car service can be linked together because they both
have a licence plate number which is unique to the car.

## Scope of application

Application is not complete or even usable. 
Code shows the main selling point of this project and that is to take a document,
transform it into a text and send it AI model that will transform it into organized output as defined by user.

## Limitations of current implementation

The CRUD is not complete. there are only create endpoints.

Only pdf file formats are supported.

No dockerized deployment.

Tests are not present.

Performance is bad because of using Postgres for search in json fields. Switching to Elasticsearch would help a lot here.
There are actually mistakes that degrade performance like the amount of IO operations when searching for duplicates.

MinIO dosent have a way of cleaning up files. If you delete a Storage Item, the file is still present in MinIO.

GraphQL schema is not documented.

## Future Steps

- Weaviate for user search across all stored files.
- Elasticsearch for efficient searching and filtering.
- Enable set notifications

## Used Reasoning Model

Gemini 2.0 Flash Thinking Experimental

## How to run

1. fill api key and project id in .env file
2. start docker containers
```bash
docker compose up
```
3. run the application
```bash
./mvnw spring-boot:run
```

## Access

http://localhost:8080/graphiql

## Resources for testing

checkout scripts folder. there are scripts for sending requests and a script for deleting db.