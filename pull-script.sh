#!/bin/bash

path=$(dirname "$(readlink -f "$0")")
# equals ./orga
cd $path
git pull

cd ./../issue-service
git pull
 
cd ./../project-service
git pull
 
cd ./../template-service
git pull

cd ./../user-service
git pull
