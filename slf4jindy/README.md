[![][maven img]][maven]
[![][travis img]][travis]
# SLF4Jindy

A jar containing an slf4j binding (logback), configured dynamically using jindy and with a bunch of slf4j bridging libs.

### Usage
Add SLF4Jindy dependency to your project

```xml
<dependency>
    <groupId>org.irenical.slf4j</groupId>
    <artifactId>slf4jindy</artifactId>
    <version>1.3.0</version>
</dependency>
```

...along with a Jindy binding, such as:
```xml
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-archaius-consul</artifactId>
    <version>2.1.1</version>
</dependency>
```

You can learn more about Jindy at https://github.com/irenical/jindy

SLF4Jindy will try to read the following properties. The values bellow are the default ones.
```properties
log.level=DEBUG

log.console.enabled=true
log.console.pattern=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

log.file.enabled=false
log.file.pattern=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
log.file.backupdatepattern=%d{yyyy-MM-dd}
log.file.path=./log/
log.file.maxbackups=5
```

Logging level per package can be achieved by suffixing the "log.level" property with the package name.
```properties
log.level.com.company.mypackage=DEBUG
log.level.com.company.some.nagging.package=ERROR
```

[maven]:http://search.maven.org/#search|gav|1|g:"org.irenical.slf4j"%20AND%20a:"slf4jindy"
[maven img]:https://maven-badges.herokuapp.com/maven-central/org.irenical.slf4j/slf4jindy/badge.svg

[travis]:https://travis-ci.org/irenical/slf4jindy
[travis img]:https://travis-ci.org/irenical/slf4jindy.svg?branch=master
