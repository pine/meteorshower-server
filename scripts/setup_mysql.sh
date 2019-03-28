#!/bin/bash

set -eu -o pipefail

sql='DROP DATABASE IF EXISTS `meteorshower`;'
echo "$sql"
echo "$sql" | mysql -u root

sql='DROP DATABASE IF EXISTS `meteorshower_test`;'
echo "$sql"
echo "$sql" | mysql -u root

sql='CREATE DATABASE `meteorshower`;'
printf "\n$sql\n"
printf "\n$sql\n" | mysql -u root

sql='CREATE DATABASE `meteorshower_test`;'
echo "$sql"
echo "$sql" | mysql -u root

sql=$(cat "./lib/auth/src/test/resources/tables.sql")
sql="\n"'USE `meteorshower`;'"\n\n$sql"
printf "$sql"
printf "$sql" | mysql -u root
