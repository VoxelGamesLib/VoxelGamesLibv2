# Contributing

## Translations

It helps to translate the framework's messages in other languages and locales.

Feel free to PR any new locale resource files.

## Contributing code

### Guidelines

* Code style: Use the formatting style located in the root of the project.
* Javadocs: Every public method needs to have javadoc annotations
* Comments: Please throw in comments in your code where appropriate so other developers can understand it better.

## Building

See [README.md](README.md)

## Dev Setup

### Test Server

An optimised test server submodule is available for use with VoxelGamesLib. You can fetch it [here](https://github.com/VoxelGamesLib/testserver)

You will need to grab a Spigot jar to run. You can set up the server as such, in IntelliJ IDEA:

![IntelliJ Dev Setup](https://i.imgur.com/GTxsHjO.png)

`mvn install` will automatically install the compiled plugin in `/testserver/plugins` for your convenience.

### Maven Repository

VoxelGamesLib can be found on the https://voxelgameslib.github.io/VoxelGamesLibv2/mvn-repo

Maven:
```
<repositories>
    <repository>
        <id>VGL</id>
        <url>https://voxelgameslib.github.io/VoxelGamesLibv2/mvn-repo/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <group>com.voxelgameslib</group>
        <artifactId>voxelgameslib</artifactId>
        <version>2.0-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Gradle:
```
repositories {
    maven { url "https://voxelgameslib.github.io/VoxelGamesLibv2/mvn-repo" }
}

dependencies {
    compileOnly group: 'com.voxelgameslib', name: 'voxelgameslib', version: '2.0-SNAPSHOT'
}
```
