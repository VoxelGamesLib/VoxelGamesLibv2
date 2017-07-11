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

### Submodules

This project includes some games as submodules. to work with them run 
`git submodule update --init --recursive`

### Test Server

An optimised test server submodule is packaged with VoxelGamesLib.

You will need to grab a Spigot jar to run. You can set up the server as such, in IntelliJ IDEA:

![IntelliJ Dev Setup](https://i.imgur.com/GTxsHjO.png)

`mvn install` will automatically install the compiled plugin in `/testserver/plugins` for your convenience.

### Maven Repository

VoxelGamesLib can be found on the [indices.io repository](https://nexus.indices.io).

```
<repositories>
    <repository>
        <id>indices</id>
        <url>https://nexus.indices.io/content/groups/public/</url>
    </repository>
</repositories>
```