#!/bin/bash
rm -rf deploy-stuff
mkdir deploy-stuff

# config
git config --global user.email "nobody@nobody.org"
git config --global user.name "Travis CI"

# copy over stuff we want to deploy
cp -R build/dependencyUpdates/. deploy-stuff/

# deploy
cd deploy-stuff
git init
git add .
git commit -m "Deploy to Github Pages"
git push --force --quiet "https://${GITHUB_TOKEN}@$github.com/VoxelGamesLib/VoxelGamesLibv2.git" master:gh-pages > /dev/null 2>&1
