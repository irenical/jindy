[![Build Status](https://travis-ci.org/irenical/jindy.svg?branch=master)](https://travis-ci.org/irenical/jindy)

Jindy is a configuration API for Java.

Wether you use a database, a properties file or framework such as Apache Commons Configuration, Netflix's Archaius or one of your own. Chances are you have to know which while you code your stuff.
Much like in logging, configuration is needed in pretty much everyting you do, and much like in logging you probably only need a simple stable API.

Jindy tries to solve this, by offering a simple API and a way to bind your implementation on runtime.
It was inspired by SLF4J logging framework.

Dirty one liner 
```java
ConfigFactory.getConfig().getString("db.host");
```

A more sane use case 
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

To use Jindy you need both the API and an actual implementation in your classpath. Note that some bindings might not support all features, namely dynamic properties.

```xml
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-api</artifactId>
    <version>1.1.0</version>
</dependency>
<dependency>
    <groupId>org.irenical.jindy</groupId>
    <artifactId>jindy-commons-impl</artifactId>
    <version>1.1.0</version>
</dependency>
```
