# VoxelGamesLib v2

[![pipeline status](https://github.com/VoxelGamesLib/VoxelGamesLibv2/workflows/Java%20CI/badge.svg)](https://github.com/VoxelGamesLib/VoxelGamesLibv2/actions)
[![codebeat badge](https://codebeat.co/badges/fca40169-ef87-4f2a-af71-5524970eb058)](https://codebeat.co/projects/github-com-voxelgameslib-voxelgameslibv2-master)
[![license](https://img.shields.io/github/license/VoxelGamesLib/VoxelGamesLibv2.svg)](LICENSE)
[![Coding time tracker](https://wakatime.com/badge/github/VoxelGamesLib/VoxelGamesLibv2.svg)](https://wakatime.com/badge/github/VoxelGamesLib/VoxelGamesLibv2)

VoxelGamesLib is a powerful, feature-packed, abstract and expandable Minecraft minigames framework. VoxelGamesLib empowers game developers, designers, and map builders to channel their creativity into crafting captivating and enjoyable games. By providing a comprehensive set of APIs and extensive support for designers and builders, it alleviates the need for repetitive coding tasks and minimizes concerns about potential bugs. The development process becomes a seamless and enjoyable experience, allowing developers to concentrate on what's important. Gameplay.

# Features

VoxelGamesLib provides developers and designers a wide-set of features to be implemented into their game. Such as:

* Elo-Based Matching
* Multiple Game Modes
* Server-Admin Support
* Stats-Tracking
* Easy Map Configurations

# Download

You can always download the latest version [here](https://github.com/VoxelGamesLib/VoxelGamesLibv2/blob/gh-pages/voxelgameslib-2.0-SNAPSHOT-all.jar?raw=true).

# Documentation

Documentation is located [here](https://voxelgameslib.github.io/docs/)  
Javadocs can be found [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/javadoc/)  

# Building

VoxelGamesLib uses [Maven](https://maven.apache.org/) as our build system. To compile:

* Install [Git](https://git-scm.com/downloads) and [Maven](https://maven.apache.org/)
* Clone the repository `git clone https://github.com/VoxelGamesLib/VoxelGamesLibv2.git`
* Run `git submodule update --init --recursive` to clone all submodule if you want to
* Run `mvn`

VoxelGamesLib is also heavily dependent on [Guice](https://github.com/google/guice), a dependency injection framework. 
A useful add-on plugin for VoxelGamesLib is [Bukkit](https://dev.bukkit.org/), an open source server modification tool.

# Usage

The basics of developing a game are including [features](https://voxelgameslib.github.io/docs/components/features/) and developing [phases](https://voxelgameslib.github.io/docs/developer-area/how-to-write-a-phase/). The basic format of a game should look similar to this:

Create a game class and constructor

`@GameInfo(name = "NewGame", author = "", version = "", description = "Game Description")
public class NewGame extends AbstractGame {
    public newGame() {
        super(newGamePlugin.GAMEMODE);
    }`

Next you want to include two methods `initGameFromModule` and `initGameFromDefinition`. Include your phases and features within `initGameFromModule`.

`@Override // For more information check out Guice Documentation linked above
    public void initGameFromDefinition(@Nonnull GameDefinition gameDefinition) {
        super.initGameFromDefinition(gameDefinition);
        loadMap(); // Loads the lobby map
    }
@Override
    public void initGameFromModule() {
        // Add Features
        // Add Phases
        loadMap();
    }`
    
To start a game run `/game start <gamemode>`.
Have fun building!

# Chat

You can find the active contributors on IRC or [Discord](https://discord.gg/VsZH2rR).
IRC: irc.spi.gt #minidigger (or [webchat](https://s.minidigger.me/irc)).

# Repository

The repository is located at `https://repo.minidigger.me/repository/voxelgameslib/`

Gradle:
```
repositories {
    maven { url 'https://repo.minidigger.me/repository/voxelgameslib/' }
}

dependencies {
    compileOnly group: 'com.voxelgameslib', name: 'voxelgameslib', version: '1.0.0-SNAPSHOT'
}
```

Maven:
```
<repositories>
    <repository>
        <id>voxelgameslib</id>
        <url>https://repo.minidigger.me/repository/maven-public/</url>
    </repository>
</repositories>


```

# Contributing

We welcome and encourage contributions to VGL. All development happens in the open, here on GitHub. Thanks to all our contributors for new features, bug fixes and other improvements.

For contributing information, see: [Contributing.md](CONTRIBUTING.md)

Dependency report can be found [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/report.txt)  
Test results can be found here [here](https://voxelgameslib.github.io/VoxelGamesLibv2/VGL/tests/test/)  
Contact us: contributors@voxelgameslib.com

# Credit

Author: Martin Benndorf 
Website: https://benndorf.dev/
Contact: admin@minidigger.me

# License

This project is [MIT](LICENSE) licensed.
