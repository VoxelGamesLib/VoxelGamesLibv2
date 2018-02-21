#!/usr/bin/env bash
echo "checking for paper"
FILE="/home/travis/.m2/repository/com/destroystokyo/paper/paper/1.12.2-R0.1-SNAPSHOT/paper-1.12.2-R0.1-SNAPSHOT.jar"
if [ $FILE does exist ]; then
   echo "Paper found, exiting"
   exit 0
fi

echo "downloading paper"
git clone --depth 1 https://github.com/PaperMC/Paper.git
cd Paper
./paper jar