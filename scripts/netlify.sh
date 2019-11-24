#!/usr/bin/env bash

echo "Setup git"
git config --global user.email "vglbot@minidigger.me"
git config --global user.name "VoxelGamesLibBot"

echo "Cloning Submodules"
git submodule update --init --recursive

echo "Running maven"
./mvnw -B package

echo "copy stuff to deploy"

mkdir deploy-stuff

declare -a paths=("VoxelGamesLib/" "games/1vs1/" "games/Deathmatch/" "games/Hub/" "tools/ChatMenuAPI/" "tools/commandline-tools/" "tools/KVGL/" "tools/dependencies/")
declare -a names=("VGL" "1vs1" "Deathmatch" "Hub" "ChatMenuAPI" "commandline-tools" "KVGL" "dependencies")

arraylength=${#paths[@]}

for (( i=1; i<${arraylength}+1; i++ ));
do
  echo $i " / " ${arraylength}
  #cp -R ${paths[$i-1]}build/dependencyUpdates/. deploy-stuff/${names[$i-1]}
  cp -R ${paths[$i-1]}build/apidocs/. deploy-stuff/${names[$i-1]}/javadoc
  #cp -R ${paths[$i-1]}build/reports/. deploy-stuff/${names[$i-1]}
  #cp -R ${paths[$i-1]}build/libs/. deploy-stuff/${names[$i-1]}
done

echo "create index"
pip install mako
python scripts/make_index.py --header "VGL Deployments" deploy-stuff
