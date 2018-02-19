echo 'wget cli.jar'
wget https://github.com/VoxelGamesLib/VoxelGamesLibv2/raw/gh-pages/commandline-tools/tools/commandline-tools-1.0-SNAPSHOT.jar -O cli.jar
echo 'run cli.jar'
java -jar cli.jar -generateDocs -docsFolder docs
echo 'push'
cd docs
git push
