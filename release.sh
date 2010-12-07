#!/bin/bash

mvn release:prepare --batch-mode -DignoreSnapshots=true -DautoVersionSubmodules=true
mvn release:perform
mvn release:clean

