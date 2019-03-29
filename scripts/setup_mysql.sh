#!/bin/bash

set -eu -o pipefail


# ----- DROP DATABASE -------------------------------------

sql='DROP DATABASE IF EXISTS `meteorshower`;'
echo "$sql"
echo "$sql" | mysql -u root

sql='DROP DATABASE IF EXISTS `meteorshower_test`;'
echo "$sql"
echo "$sql" | mysql -u root


# ----- CREATE DATABASE -----------------------------------

sql='CREATE DATABASE `meteorshower`;'
printf "\n$sql\n"
printf "\n$sql\n" | mysql -u root

sql='CREATE DATABASE `meteorshower_test`;'
echo "$sql"
echo "$sql" | mysql -u root


# ----- CREATE TABLE : auth -------------------------------

sql=$(cat "./lib/auth/src/test/resources/drop_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -u root

sql=$(cat "./lib/auth/src/test/resources/create_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -u root


# ----- CREATE TABLE : setting ----------------------------

sql=$(cat "./lib/setting/src/test/resources/drop_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -u root

sql=$(cat "./lib/setting/src/test/resources/create_tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql\n"
printf "$sql"
printf "$sql" | mysql -u root
