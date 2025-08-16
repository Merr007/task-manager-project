#!/bin/bash
cd ..
echo Clean previous runs
rm -rf changelog
echo Clean previous builds...
docker compose rm --stop --force
echo Assembling changelog
mkdir changelog
cp -vR ./src/main/resources/changesets/. changelog/
echo Running local database...
docker compose up -d
docker logs --tail=25 -f liquibase
echo Deleting changelog
rm -rf changelog