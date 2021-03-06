[![][maven img]][maven]
[![][travis img]][travis]
[![][codecov img]][codecov]

### What and Why
Jindy is a configuration API for Java.

![alt text][knob]  


Whether you use a database, a properties file or a framework such as Apache Commons Configuration, Netflix's Archaius 
or one of your own, chances are you are forced to know which while you code your stuff. Much like in logging, 
configuration is needed in pretty much everything you do, and much like in logging you probably only need a simple 
stable API.

Since changes in jindy-api have high impact in the downstream, the API was designed to be a sensible superset of multiple java configuration implementations both in features and naming. Breaking changes are released with an incremented major version. API increments might occurr in a minor version and there is currently no check for bytecode retro-compatibility, so some care is advised. Patch versions don't change the jindy-api's interfaces whatsoever.

Jindy tries to solve this, by offering a simple API and a way to bind your implementation on runtime. It was inspired 
by the SLF4J logging framework (https://www.slf4j.org).

### Usage

To use Jindy you need both the API and an actual implementation in your classpath. Note that some bindings might not 
support all features, namely dynamic properties.

```xml
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-apacheconfig-impl</artifactId>
    <version>3.0.2</version>
</dependency>
```

Throw a config.properties file to your src/main/resources
```properties
db.host=localhost
db.port=5432
```

Dirty one liner 
```java
ConfigFactory.getConfig().getString("db.host");
```

A common use case 
```java
    private static final Config CONFIG = ConfigFactory.getConfig();

    public void setupMyDatabase(){
      String dbHost = CONFIG.getString("db.host");
      ...
    }
```

Strongly typed properties with default values and typical is-property-defined check
```java
    private static final Config CONFIG = ConfigFactory.getConfig();
    
    public void setupMyDatabase(){
        try{
            String dbHost = CONFIG.getMandatoryString("db.host");
            int dbPort = CONFIG.getInt("db.port",5432);
            ...
        } catch(ConfigNotFoundException e){
            ...
        }
    }
```

Reacting to configuration changes
```java
   private static final Config CONFIG = ConfigFactory.getConfig();
    
    
    public MyDatabaseSetup() {
        CONFIG.listen("db.host", this::setupMyDatabase);
    }
    
    public void setupMyDatabase(String changedKey){
        try{
            String dbHost = CONFIG.getMandatoryString("db.host");
            int dbPort = CONFIG.getInt("db.port",5432);
            ...
        } catch(ConfigNotFoundException e){
            ...
        }
    }
```
For dynamic behaviour to kick in, you must use a binding that actually implements it (ex: consul, zookeeper, etcd, ...), as of today jindy-apacheconfig-impl does not.

### Configuration Sources
Depending on the binding you chose, the supported configuration sources might vary. The Consul-backed implementation provided by the Jindy project will behave as Netflix's Archaius, trying to read configuration properties from Consul key-value API, the file located at the property archaius.configurationSource.additionalUrls (passed to the JVM with -D), the config.properties file and the system's properties in that order. In this implementation, changes in Consul will instantly trigger any relevant listener registered with Jindy.


### Limitations
You cannot have multiple implementations/bindings at the same time. In order to avoid this, your libraries should only depend on the jindy-api, leaving the implementation choice to the final application (war, executable jar, application container, etc... ).

[knob]:https://www.irenical.org/jindy/jindy.png "Rotating a knob didn't reboot the whole radio"

[maven]:http://search.maven.org/#search|gav|1|g:"org.irenical.jindy"%20AND%20a:"jindy-api"
[maven img]:https://maven-badges.herokuapp.com/maven-central/org.irenical.jindy/jindy-api/badge.svg

[travis]:https://travis-ci.org/irenical/jindy
[travis img]:https://travis-ci.org/irenical/jindy.svg?branch=master

[codecov]:https://codecov.io/gh/irenical/jindy
[codecov img]:https://codecov.io/gh/irenical/jindy/branch/master/graph/badge.svg
