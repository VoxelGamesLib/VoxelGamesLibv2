#!/bin/bash
echo "clean deploy folder"
rm -rf deploy-stuff

git clone --depth 10 -b gh-pages "https://${GITHUB_TOKEN}@github.com/VoxelGamesLib/VoxelGamesLibv2.git" deploy-stuff

echo "setup git"

git config --global user.email "vglbot@minidigger.me"
git config --global user.name "VoxelGamesLibBot"

echo "copy stuff to deploy"

declare -a paths=("" "games/1vs1/" "games/Deathmatch/" "games/Hub/" "tools/ChatMenuAPI/" "tools/commandline-tools/" "tools/KVGL/")
declare -a names=("VGL" "1vs1" "Deathmatch" "Hub" "ChatMenuAPI" "commandline-tools" "KVGL")

arraylength=${#paths[@]}

for (( i=1; i<${arraylength}+1; i++ ));
do
  echo $i " / " ${arraylength}
  cp -R ${paths[$i-1]}build/dependencyUpdates/. deploy-stuff/${names[$i-1]}
  cp -R ${paths[$i-1]}build/docs/javadoc/. deploy-stuff/${names[$i-1]}/javadoc
  cp -R ${paths[$i-1]}build/reports/. deploy-stuff/${names[$i-1]}
  cp -R ${paths[$i-1]}build/libs/. deploy-stuff/${names[$i-1]}
done

echo "deploy to maven repo"
declare -a jars=(
                "build/libs/voxelgameslib-2.0-SNAPSHOT-all.jar"
                "games/1vs1/build/libs/games/1vs1-1.0-SNAPSHOT.jar"
                "games/Deathmatch/build/libs/games/Deathmatch-1.0-SNAPSHOT.jar"
                "games/Hub/build/libs/games/Hub-1.0-SNAPSHOT.jar"
                "tools/ChatMenuAPI/build/libs/tools/ChatMenuAPI-1.0-SNAPSHOT.jar"
                "tools/commandline-tools/build/libs/tools/commandline-tools-1.0-SNAPSHOT.jar"
                "tools/KVGL/build/libs/tools/KVGL-1.0-SNAPSHOT.jar"
                )
arraylength=${#jars[@]}
for (( i=1; i<${arraylength}+1; i++ ));
do
  echo $i " / " ${arraylength}
  mvn deploy:deploy-file -Dfile=${jars[$i-1]} -DpomFile=pom.xml  -Durl=file://${TRAVIS_BUILD_DIR}/deploy-stuff/mvn-repo
done

echo "create index"
sudo pip install mako
python scripts/make_index.py --header "VGL Deployments" deploy-stuff

echo "add to repo"
cd deploy-stuff
git add .
echo "commit"
git commit -m "Deploy to Github Pages (VGL)"
echo "push"
git push --force "https://${GITHUB_TOKEN}@github.com/VoxelGamesLib/VoxelGamesLibv2.git" gh-pages:gh-pages
echo "DONE"