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

VoxelGamesLib uses [Maven](https://maven.apache.org/) as our build system. To compile:

* Install Git and Maven
* Clone the repository
* Run `git submodule update --init --recursive` to clone all submodule if you want to
* Run `mvn`

# Chat

You can find the active contributors on IRC or [Discord](https://s.minidigger.me/discord).
IRC: irc.spi.gt #minidigger (or [webchat](https://s.minidigger.me/irc)).

# Repository

The repository is located at `https://voxelgameslib.github.io/VoxelGamesLibv2/mvn-repo`

Gradle:
URL:
```
repositories {
    maven { url 'https://voxelgameslib.github.io/VoxelGamesLibv2/mvn-repo' }
}

dependencies {
    compileOnly group: 'com.voxelgameslib', name: 'voxelgameslib', version: '2.0-SNAPSHOT'
}
```

# Contributing

We welcome and encourage contributions to VGL. All development happens in the open, here on GitHub. Thanks to all our contributors for new features, bug fixes and other improvements.

For contributing information, see: [Contributing.md](CONTRIBUTING.md)

Dependency report can be found [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/report.txt)  
Test results can be found here [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/tests/test/)  

# License

[MIT](LICENSE)
