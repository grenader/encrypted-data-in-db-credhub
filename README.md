# encrypted-data-in-db-credhub
An example of how to seamlessly encrypt sensitive data stored in a DB, plus using credhub on PCF

## Preparing a database

### Login to mySQL client with admin user:
e.g. #> mysql -u root -p <ROOT_PASSWORD>

### Prepare an empty DB:
mysql> create database encrypted_data;
Query OK, 1 row affected (0.00 sec)

mysql> grant all on encrypted_data.* to 'docker'@'%';
Query OK, 0 rows affected (0.00 sec)