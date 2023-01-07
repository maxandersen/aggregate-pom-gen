///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 17+
//FILES application.properties=aggregatePom.properties
//DEPS io.quarkus:quarkus-bom:${quarkus.version:2.11.2.Final}@pom
//DEPS io.quarkus:quarkus-picocli
//DEPS io.quarkus:quarkus-qute

import static java.nio.file.Files.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.qute.Qute;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@CommandLine.Command(description = "Generate an aggregate POM file for direct sub-folders",
                    version = "aggregatePom 0.1")
public class main implements Runnable {

    @Option(names = { "--groupId" }, description = "The groupId", defaultValue = "org.acme")
    String groupId;

    @Option(names = { "--artifactId" }, description = "The artifactId", defaultValue = "artifactId")
    String artifactId;

    @Option(names = { "--version" }, description = "The version", defaultValue = "999-SNAPSHOT")
    String version;

    @Inject
    Logger log;

    String pomTemplate = """
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
            <groupId>{GroupId}</groupId>
                <artifactId>{ArtifactId}</artifactId>
                <version>{Version}</version>
                <packaging>pom</packaging>
                <modelVersion>4.0.0</modelVersion>
                <modules>
                {#each Modules}
                        <module>{it}</module>
                {/each}
                </modules>
        </project>
        """;

    @Override
    public void run() {
            try {
                 modules = new ArrayList<>();
    
                list(Path.of(".")).forEach(p -> {
                    if (isDirectory(p) && exists(p.resolve("pom.xml"))) {
                        modules.add(p.getFileName().toString());
                    }
                });
    
                writeString(Path.of("pom.xml"),
                        Qute.fmt(pomTemplate)
                                .data("GroupId", groupId)
                                .data("ArtifactId", artifactId)
                                .data("Version", version)
                                .data("Modules", modules).render(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
    
            } catch (IOException e) {
                log.fatal(e);
            }
        }
}
