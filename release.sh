#!/bin/bash

if [ -z $1 ] ; then
  echo "Usage: release.sh <version>"
  exit 1
fi

git checkout -b _release master
mvn release:update-versions -DdevelopmentVersion=$1 --batch-mode
git commit -am "Updated to version $1"
git tag -a "Releasing $1"
mvn clean deploy -Dmaven.test.skip.exec=true
git push --tags
git checkout master
git branch -D _release

