#!/bin/bash
echo "clean deploy filder"
rm -rf deploy-stuff
mkdir deploy-stuff

# config
echo "setup git"
git config --global user.email "nobody@nobody.org"
git config --global user.name "Travis CI"

# copy over stuff we want to deploy
echo "copy stuff to deploy"
cp -R build/dependencyUpdates/. deploy-stuff/

# deploy
echo "create repo"
cd deploy-stuff
git init
git add .
echo "commit"
git commit -m "Deploy to Github Pages"
echo "push"
git push --force --quiet "https://${GITHUB_TOKEN}@$github.com/VoxelGamesLib/VoxelGamesLibv2.git" master:gh-pages > /dev/null 2>&1
