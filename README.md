[![][maven img]][maven]
[![][travis img]][travis]

### What and Why
Jindy is a configuration API for Java.

Whether you use a database, a properties file or framework such as Apache Commons Configuration, Netflix's Archaius 
or one of your own, chances are you are forced to know which while you code your stuff. Much like in logging, 
configuration is needed in pretty much everything you do, and much like in logging you probably only need a simple 
stable API.

Jindy tries to solve this, by offering a simple API and a way to bind your implementation on runtime. It was inspired 
by the SLF4J logging framework.

### Usage
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

All of that with dynamic behaviour
```java
   private static final Config CONFIG = ConfigFactory.getConfig();
    
    
    public MyDatabaseSetup() {
        CONFIG.listen("db.host", this::setupMyDatabase);
    }
    
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

To use Jindy you need both the API and an actual implementation in your classpath. Note that some bindings might not 
support all features, namely dynamic properties.

```xml
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-apacheconfig-impl</artifactId>
    <version>2.0.0</version>
</dependency>
```

[maven]:http://search.maven.org/#search|gav|1|g:"org.irenical.jindy"%20AND%20a:"jindy-api"
[maven img]:https://maven-badges.herokuapp.com/maven-central/org.irenical.jindy/jindy-api/badge.svg

[travis]:https://travis-ci.org/irenical/jindy
[travis img]:https://travis-ci.org/irenical/jindy.svg?branch=master
