#!/bin/bash

set -eu -o pipefail


# ----- DROP DATABASE -------------------------------------

sql='DROP DATABASE IF EXISTS `meteorshower`;'
echo "$sql"
echo "$sql" | mysql -h 127.0.0.1 -u root

sql='DROP DATABASE IF EXISTS `meteorshower_test`;'
echo "$sql"
echo "$sql" | mysql -h 127.0.0.1 -u root


# ----- CREATE DATABASE -----------------------------------

sql='CREATE DATABASE `meteorshower`;'
printf "\n$sql\n"
printf "\n$sql\n" | mysql -h 127.0.0.1 -u root

sql='CREATE DATABASE `meteorshower_test`;'
echo "$sql"
echo "$sql" | mysql -h 127.0.0.1 -u root


# ----- CREATE TABLE : auth -------------------------------

sql=$(cat "./lib/auth/src/test/resources/drop_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -h 127.0.0.1 -u root

sql=$(cat "./lib/auth/src/test/resources/create_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -h 127.0.0.1 -u root


# ----- CREATE TABLE : setting ----------------------------

sql=$(cat "./lib/setting/src/test/resources/drop_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -h 127.0.0.1 -u root

sql=$(cat "./lib/setting/src/test/resources/create_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -h 127.0.0.1 -u root
