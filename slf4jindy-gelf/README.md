[![][maven img]][maven]
[![][travis img]][travis]
# SLF4Jindy + Gelf

Extending [SLF4Jindy](https://github.com/irenical/slf4jindy), adding a Gelf logger.

### Usage
Remove slf4jindy dependency from your project and use the following instead

```xml
<dependency>
    <groupId>org.irenical.slf4j</groupId>
    <artifactId>slf4jindy-gelf</artifactId>
    <version>1.3.0</version>
</dependency>
```

...along with a [Jindy](https://github.com/irenical/jindy) binding, such as:
```xml
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-archaius-consul</artifactId>
    <version>2.1.1</version>
</dependency>
```

slf4jindy-gelf supports all of slf4jindy properties, plus the following
```properties
log.gelf.enabled=false
log.gelf.host=
log.gelf.port=
```
Host and port are mandatory with no defaults.  
Slf4jindy-gelf automatically reports the context metadata obtained through org.irenical.jindy.ConfigFactory.getContext().

[maven]:http://search.maven.org/#search|gav|1|g:"org.irenical.slf4j"%20AND%20a:"slf4jindy-gelf"
[maven img]:https://maven-badges.herokuapp.com/maven-central/org.irenical.slf4j/slf4jindy-gelf/badge.svg

[travis]:https://travis-ci.org/irenical/slf4jindy-gelf
[travis img]:https://travis-ci.org/irenical/slf4jindy-gelf.svg?branch=master
