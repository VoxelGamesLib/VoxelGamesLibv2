#!/bin/bash
echo "clean deploy filder"
rm -rf deploy-stuff
mkdir deploy-stuff

# config
echo "setup git"
git config --global user.email "vglbot@minidigger.me"
git config --global user.name "VoxelGamesLibBot"

# copy over stuff we want to deploy
echo "copy stuff to deploy"
cp -R build/dependencyUpdates/. deploy-stuff/
cp -R build/docs/javadoc/. deploy-stuff/
cp -R build/reports/. deploy-stuff/
cp -R build/libs/. deploy-stuff/

# create mvn repo
mvn install:install-file -Dfile=build/libs/voxelgameslib-2.0-SNAPSHOT.jar -DpomFile=pom.xml  -DlocalRepositoryPath=deploy-stuff/mvn-repo

# deploy
echo "create repo"
cd deploy-stuff
git init
git add .
echo "commit"
git commit -m "Deploy to Github Pages"
echo "push"
git push --force "https://${GITHUB_TOKEN}@github.com/VoxelGamesLib/VoxelGamesLibv2.git" master:gh-pages
