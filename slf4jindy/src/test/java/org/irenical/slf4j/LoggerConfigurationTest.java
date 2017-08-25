package org.irenical.slf4j;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.irenical.notslf4j.OtherClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

public class LoggerConfigurationTest {

  @Test
  public void doSomeTests() {
    System.setProperty("archaius.deployment.applicationId", "test");
    System.setProperty("dynamicConfig", "false");

    LoggerConfigurator configurator = new LoggerConfigurator();
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    configurator.setContext(context);
    context.reset();
    configurator.configure(context);

    Logger logHere = LoggerFactory.getLogger(LoggerConfigurationTest.class);
    Logger logThere = LoggerFactory.getLogger(OtherClass.class);
    Config conf = ConfigFactory.getConfig();

    logHere.debug("here: BEFORE ALL");
    logThere.debug("there: BEFORE ALL");

    conf.setProperty("log.level", "INFO");
    logHere.debug("here: AFTER DISABLE GLOBAL");
    logThere.debug("there: AFTER DISABLE GLOBAL");

    conf.setProperty("log.level.org.irenical.slf4j", "DEBUG");
    logHere.debug("here: AFTER ENABLE LOCAL");
    logThere.debug("there: AFTER ENABLE LOCAL");
  }

}
