# SmartStorage

AI powered document management application.

## TechStack

PostgreDB   
SpringAI   
MinIO   
GraphQL   
Docker
RabbitMQ
Java 25.0.2

## Description

(Vision)
The application will be able to automatically mine user defined data from uploaded documents. 
User defines Storage Section which stores as many attributes as user wants to be extracted from the document. This is the
only thing the user needs to do before he starts throwing documents at the application.
Applications upload endpoint will save the file in original form into MinIO storage. 
Right after that the application fill out an Storage Item object with the data extracted from the document.

Uploaded files are linked to Storege Item. the item can hole multiple files. This is possible because when creating a new
Storage Section, user can select fields, in the variable array of atributes, that has to be unique. Based on this information
the application detects common Storage Item between multiple uploaded documents.

for example: a document about car insurance and a document about car service can be linked together because they both
have a licence plate number which is unique to the car.

## Scope of application

The application

## W

### AI based document parsing
Application will enable to store pdf documents. These pdf files will be parsed into table record.

### AI chat
User will be able to asi AI to find an information

### notifications
get notification when important date is near. For example end of car warranty.

## Used Reasoning Model

Gemini 2.0 Flash Thinking Experimental

## Functional Requirements
- User will be able to perform CRUD operations on documents
- User will be able to ask questions to AI about the documents
- User will be able to get notifications when important date is near. For example end of car warranty.
- User will be able to perform CRUD operations on Document Sections.

## Run the application

## Access

http://localhost:8080/graphiql