#Created mutile endpoints under AdminController and EmployeeController
When the Logon API is called (For bothAdmin and Employee) username is validate in DB (Not password) and a JWT is generated
This JWT can be used in the subsequent requests

Data model:

Collections : InternalUser , transaction,accountType, customer


PREREQUISITE

Mongo DB need to be installed in system and it should be up 

Execute the below queries


use globalbank;

db.internalUser.insert({
 "_id":"adm_1",
"userName":"adm_1",
 "firstName":"abc",
 "lastName":"xyz",
 "email":"abc@abc.com",
 "mobile":"88888888",
"userType":"Admin"
})