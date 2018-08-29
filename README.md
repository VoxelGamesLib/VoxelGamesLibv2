# VoxelGamesLib v2

[![pipeline status](https://gitlab.com/VoxelGamesLib/VoxelGamesLibv2/badges/master/pipeline.svg)](https://gitlab.com/VoxelGamesLib/VoxelGamesLibv2/commits/master)
[![codebeat badge](https://codebeat.co/badges/fca40169-ef87-4f2a-af71-5524970eb058)](https://codebeat.co/projects/github-com-voxelgameslib-voxelgameslibv2-master)
[![license](https://img.shields.io/github/license/VoxelGamesLib/VoxelGamesLibv2.svg)](LICENSE)

Powerful, feature-packed, abstract and expandable Minecraft minigames framework.

# Download

You can always download the latest version [here](https://github.com/VoxelGamesLib/VoxelGamesLibv2/blob/gh-pages/voxelgameslib-2.0-SNAPSHOT-all.jar?raw=true).

# Documentation

Documentation is located [here](https://voxelgameslib.github.io/docs/)  
Javadocs can be found [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/javadoc/)  

# Building

VoxelGamesLib uses [Gradle](https://gradle.org/) as our build system. To compile:

* Install Git and Gradle
* Clone the repository
* Run `git submodule update --init --recursive` to clone all submodule if you want to
* Run `gradle clean build`

# Chat

You can find the active contributors on IRC. irc.spi.gt #minidigger (or [webchat](https://s.minidigger.me/irc))  
Opon popular demand, I also setup a discord ([invite link](https://s.minidigger.me/discord))

# Repo

I host a maven repo on github, because why not. Details below.  
URL:
```
        maven { url "https://voxelgameslib.github.io/VoxelGamesLibv2/mvn-repo" }
```
Artifact:
```
        compileOnly group: 'com.voxelgameslib', name: 'voxelgameslib', version: '2.0-SNAPSHOT'
```

# Contributing

The main point of the repository is to encourage developers to participate in the development of VoxelGamesLib. All development happens in the open, here on GitHub. We are grateful to all contributors for new features, bug fixes and other improvements.

For contributing information, see: [Contributing.md](CONTRIBUTING.md)

Dependency report can be found [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/report.txt)  
Test results can be found here [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/tests/test/)  

# License

[MIT](LICENSE)
