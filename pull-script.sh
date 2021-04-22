#!/bin/bash

maindir=${pwd}
# equals ./orga

git pull

cd ../issue-service
git pull

cd ../project-service
git pull

cd ../template-service
git pull

cd ../user-service
git pull
