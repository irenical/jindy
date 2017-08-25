package org.irenical.slf4j;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.joran.GenericConfigurator;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.irenical.jindy.Config;
import org.irenical.jindy.Config.Match;
import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class LoggerConfigurator extends GenericConfigurator implements Configurator {

  private static final String APPENDER_CONSOLE = "CONSOLE";
  private static final String APPENDER_FILE = "FILE";

  private static final String LEVEL = "log.level";

  private static final String CONSOLE_ENABLED = "log.console.enabled";
  private static final String CONSOLE_PATTERN = "log.console.pattern";

  private static final String FILE_ENABLED = "log.file.enabled";
  private static final String FILE_PATTERN = "log.file.pattern";
  private static final String FILE_NAME = "log.file.name";
  private static final String FILE_BACKUP_DATE_PATTERN = "log.file.backupdatepattern";
  private static final String FILE_PATH = "log.file.path";
  private static final String FILE_MAXBACKUPS = "log.file.maxbackups";

  private static final String DEFAULT_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
  private static final String DEFAULT_FILE_PATH = "./log/";
  private static final int DEFAULT_FILE_MAXBACKUPS = 5;
  private static final String DEFAULT_BACKUP_DATE_PATERN = "%d{yyyy-MM-dd}";

  private static final String DEFAULT_FILE_NAME = "default";

  private static final String EXT = ".log";

  private static final String SEP = "-";

  private static volatile boolean started = false;

  private final Config config = ConfigFactory.getConfig();

  public LoggerConfigurator() {
  }

  @Override
  protected void addInstanceRules(RuleStore rs) {

  }

  @Override
  protected void addImplicitRules(Interpreter interpreter) {

  }

  protected void initListeners() {
    config.listen(LEVEL, Match.PREFIX, this::updateLevel);
    config.listen(CONSOLE_ENABLED, this::updateConsole);
    config.listen(CONSOLE_PATTERN, this::updateConsole);
    config.listen(FILE_ENABLED, this::updateFile);
    config.listen(FILE_PATTERN, this::updateFile);
    config.listen(FILE_MAXBACKUPS, this::updateFile);
    config.listen(FILE_PATH, this::updateFile);
    config.listen(FILE_BACKUP_DATE_PATTERN, this::updateFile);
  }

  @Override
  public void configure(LoggerContext loggerContext) {
    if (!started) {
      setContext(loggerContext);
      initListeners();
      started = true;
    }
    loggerContext.reset();
    installJulBridge();
    updateLevel(null);
    updateConsole(null);
    updateFile(null);
  }

  private void installJulBridge() {
    // Workaround for strange ClassCircularityErrors in the JUL bridge when very
    // strange classloader hierarchies are
    // set up and logging occurs from inside classloaders themselves (eg: some
    // strange Tomcat deployments)
    try {
      Class.forName("java.util.logging.LogRecord");
    } catch (ClassNotFoundException e) {
      throw new AssertionError(e);
    }

    LoggerContext loggerContext = (LoggerContext) getContext();

    if (!SLF4JBridgeHandler.isInstalled()) {
      SLF4JBridgeHandler.removeHandlersForRootLogger();
      SLF4JBridgeHandler.install();
    }
    LevelChangePropagator julLevelChanger = new LevelChangePropagator();
    julLevelChanger.setContext(loggerContext);
    julLevelChanger.setResetJUL(true);
    julLevelChanger.start();
    loggerContext.addListener(julLevelChanger);
  }

  private void updateFile(String changedProp) {
    try {
      LoggerContext loggerContext = (LoggerContext) getContext();
      Logger logbackLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

      RollingFileAppender<ILoggingEvent> fileAppender = (RollingFileAppender<ILoggingEvent>) logbackLogger
          .getAppender(APPENDER_FILE);
      if (config.getBoolean(FILE_ENABLED, false)) {
        logbackLogger.detachAppender(fileAppender);

        fileAppender = new RollingFileAppender<>();
        fileAppender.setName(APPENDER_FILE);
        fileAppender.setContext(loggerContext);

        String file = config.getString(FILE_PATH, DEFAULT_FILE_PATH);
        if (!file.endsWith("/")) {
          file += "/";
        }

        ConfigContext context = ConfigFactory.getContext();
        String applicationId = context == null ? null : context.getApplicationId();

        String fileName = config.getString(FILE_NAME, applicationId == null ? DEFAULT_FILE_NAME : applicationId);

        fileAppender.setFile(file + fileName + EXT);

        TimeBasedRollingPolicy<ILoggingEvent> rollPolicy = new TimeBasedRollingPolicy<>();
        rollPolicy.setContext(loggerContext);
        rollPolicy.setFileNamePattern(
            file + fileName + SEP + config.getString(FILE_BACKUP_DATE_PATTERN, DEFAULT_BACKUP_DATE_PATERN) + EXT);
        rollPolicy.setMaxHistory(config.getInt(FILE_MAXBACKUPS, DEFAULT_FILE_MAXBACKUPS));
        rollPolicy.setParent(fileAppender);
        fileAppender.setRollingPolicy(rollPolicy);
        fileAppender.setTriggeringPolicy(rollPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(config.getString(FILE_PATTERN, DEFAULT_PATTERN));
        fileAppender.setEncoder(encoder);

        logbackLogger.addAppender(fileAppender);
        rollPolicy.start();
        encoder.start();
        fileAppender.start();

      } else {
        logbackLogger.detachAppender(fileAppender);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateConsole(String changedProp) {
    LoggerContext loggerContext = (LoggerContext) getContext();
    Logger logbackLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

    ConsoleAppender<ILoggingEvent> consoleAppender = (ConsoleAppender<ILoggingEvent>) logbackLogger
        .getAppender(APPENDER_CONSOLE);
    if (config.getBoolean(CONSOLE_ENABLED, true)) {
      logbackLogger.detachAppender(consoleAppender);

      consoleAppender = new ConsoleAppender<>();
      consoleAppender.setContext(loggerContext);
      consoleAppender.setName(APPENDER_CONSOLE);

      PatternLayoutEncoder encoder = new PatternLayoutEncoder();
      encoder.setContext(loggerContext);
      encoder.setPattern(config.getString(CONSOLE_PATTERN, DEFAULT_PATTERN));
      consoleAppender.setEncoder(encoder);
      encoder.start();
      consoleAppender.start();

      logbackLogger.addAppender(consoleAppender);
    } else {
      logbackLogger.detachAppender(consoleAppender);
    }
  }

  private void updateLevel(String changedProp) {
    Iterable<String> keys = config.getKeys(LEVEL);
    for (String key : keys) {
      if (key.length() > LEVEL.length() + 1) {
        String pack = key.substring(LEVEL.length() + 1);
        updateLoggerLevel(pack, config.getString(key));
      }
    }
    updateLoggerLevel(Logger.ROOT_LOGGER_NAME, config.getString(LEVEL, "DEBUG"));
  }

  private void updateLoggerLevel(String loggerName, String level) {
    LoggerContext loggerContext = (LoggerContext) getContext();
    Logger logbackLogger = loggerContext.getLogger(loggerName);
    switch (level) {
    case "ERROR":
      logbackLogger.setLevel(Level.ERROR);
      break;
    case "WARN":
      logbackLogger.setLevel(Level.WARN);
      break;
    case "INFO":
      logbackLogger.setLevel(Level.INFO);
      break;
    case "DEBUG":
      logbackLogger.setLevel(Level.DEBUG);
      break;
    case "TRACE":
      logbackLogger.setLevel(Level.TRACE);
      break;
    case "ALL":
      logbackLogger.setLevel(Level.ALL);
      break;
    default:
      logbackLogger.setLevel(Level.OFF);
    }
  }

}
