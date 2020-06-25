# encrypted-data-in-db-credhub
An example of how to seamlessly encrypt sensitive data stored in a DB, plus using credhub on PCF to store and obtain critical information

The application provides **add**, **list** and **count** operations for user and 'credit card' objects.
The operations are exposed via Web Services API build using Reactive Services.
The following API is available:

## Preparing a database
We will use Docker to run a mySQL database.

### Create a docker container
```
docker run -p 3406:3306 -d --name mysql-test -e MYSQL_ROOT_PASSWORD=11 mysql/mysql-server:5.7
```
Note that we set mySQL password to '11', you can choose any other password.
By default, mySQL is accessible from within the container only.
The container will be bounded to host port 3406. 

### Enable remote access to mySQL database
```
docker exec -it mysql bash

container> mysql -uroot -p11

mysql> CREATE DATABASE encrypted_data;
Query OK, 0 rows affected (0.00 sec)

mysql> CREATE USER 'docker'@'%' IDENTIFIED BY 'password';
Query OK, 0 rows affected (0.00 sec)

mysql> GRANT ALL PRIVILEGES ON encrypted_data.* TO 'docker'@'%';
Query OK, 0 rows affected (0.00 sec)

mysql> quit
```

First, we are creating a database called 'encrypted_data'.
Then, we added a new database usr called 'docker' with password 'password'.
This user will be able to access this new mySQL database from outside of the container.
 
## Build the application
mvn clean package
Note, that application unit tests will be run, they are using a local mySQL database that should be deployed. 
Please, look above in this instruction to see the DB configuration steps.

## References:
Accessing data with MySQL: https://spring.io/guides/gs/accessing-data-mysql/
MySQL Setup in Docker for Mac: https://dzone.com/articles/docker-for-mac-mysql-setup

# PCF Deployment
The application has several profiles.
Local and test profiles are configured to use a local database accessible on localhost.

## Use the application locally
### Create an executable jar with Maven:
```
mvn clean install
```

### Start the application
```
java -jar -Dspring.profiles.active=local target/encrypted-data-0.0.1-SNAPSHOT.jar
```
Note, that we are specifying 'local' as a Spring Profile

### Test data encryption manually
Open the following URLs in the browser:
- http://localhost:8080/hello - check whether the application is responding
- http://localhost:8080/data/creditcard/add?name=TestName&number=4121212121212&expiration=10/22&cvv=123 - insert a new credit card to the DB
- http://localhost:8080/data/creditcard/list - list all the credit card stored in the DB.

Here is the sample result of the 'list' call:
> {"id":47,"name":"TestName","number":"4121212121212","expiration":"10/22","cvv":"123"}

- http://localhost:8080/data/creditcard/db/1 - fetch from DB an encrypted credit card object with id == **1**. 

Here is the sample result of the 'creditcard/db' call:
> {"id":1,"name":"yTxDAcKeP/AcS02yE+wJTuadA56MbLsOwjoLzm339Tk=","number":"VpHm5FhECJ4gpaHoFOJISGCSKB1IvY6YJ4GfhsogKck=","expiration":"U3enyhbRrSkshS7oUqVZUw==","cvv":"123"}

The data will encrypted in the database. Here is how it will look like:
![Encrypted data](img/encrypted-table-data-sample.png?raw=true "encrypted credit_card table")
 
## Try the application on PCF
This application is also deployed on PCF.
It is using mySQL database as a service and credhub to store.
Try the following URLs:
- http://reactive-pcf-service.apps.richmond.cf-app.com/data/creditcard/add?name=TestName&number=4121212121212&expiration=10/22&cvv=123 - insert a new credit card to the DB
- http://reactive-pcf-service.apps.richmond.cf-app.com/data/creditcard/list - list all the credit card stored in the DB.
- http://reactive-pcf-service.apps.richmond.cf-app.com/data/creditcard/db/1 - fetch from DB an encrypted credit card object with id == **1**.

## CredHub configuration
The application deployed on PCF using a default profile.

### Create a credhub service instance:
```
cf create-service credhub default credhub-svc1 -c '{"dbsecret":"secret-key-90123489","name":"MY_CREDHUB_CRED","value":"ABCDEFGHIJK12345678"}'
```
The service has been configured with 'dbsecret' property.

### Link credhub service to the app:
```
cf bind-service reactive-pcf-service credhub-svc1
cf restage reactive-pcf-service
```

### Access credhub property in the application
Please, review application.properties file content.
> dbsecret=${vcap.services.credhub-svc1.credentials.dbsecret}

A value defined on credhub will be available as 'dbsecret' application property.
**AttributeEncryptor** class shows how to access a property.

## mySQL service configuration on PCF 
Please, review application.properties file content.
Database configuration properties are available via VCAP properties:
> // mySQL PCF service database configuration values, they will be filled by the platform
> dbhost=${vcap.services.mysql-test1.credentials.hostname}
> dbport=${vcap.services.mysql-test1.credentials.port}
> dbname=${vcap.services.mysql-test1.credentials.name}
> dbusername=${vcap.services.mysql-test1.credentials.username}
> dbpwd=${vcap.services.mysql-test1.credentials.password}

These properties are available from 'mysql-test1' database service.
 
 
 
 
