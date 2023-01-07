# Maven Aggregator POM Generator 

A simple tool to generate an aggregate Maven POM file from independent projects in sub-folders.

## Overview

```
$ aggregate-pom-gen --help
Usage: <main class> [--artifactId=<artifactId>] [--groupId=<groupId>]
                    [--version=<version>]
Generate an aggregate POM file for direct sub-folders
      --artifactId=<artifactId>
                            The artifactId
      --groupId=<groupId>   The groupId
      --version=<version>   The version
```

It will write a `pom.xml` file in the current folder, where each referenced module corresponds to a direct sub-folder with a `pom.xml` file.

## Installation

```
jbang app install aggregate-pom-gen@maxandersen/aggregate-pom-gen
```
