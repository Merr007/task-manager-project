#!/bin/bash
cd ..
echo Clean previous runs...
rm -rf changelog
echo Assembling changelog
cp -vR ./src/main/resources/changesets/. changelog/
echo Updating database...
docker compose run liquibase
docker logs --tail=25 -f liquibase
echo Deleting changelog
rm -rf changelog