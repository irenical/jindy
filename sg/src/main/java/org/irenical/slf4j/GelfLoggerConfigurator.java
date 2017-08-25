package org.irenical.slf4j;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import gelf4j.logback.GelfAppender;

public class GelfLoggerConfigurator extends LoggerConfigurator implements Configurator {

  private static final String APPENDER_GELF = "GELF";

  private static final String GELF_ENABLED = "log.gelf.enabled";
  private static final String GELF_HOST = "log.gelf.host";
  private static final String GELF_PORT = "log.gelf.port";

  private final Config CONFIG = ConfigFactory.getConfig();

  public GelfLoggerConfigurator() {
  }

  @Override
  protected void initListeners() {
    super.initListeners();
    CONFIG.listen(GELF_ENABLED, this::updateGelf);
    CONFIG.listen(GELF_HOST, this::updateGelf);
    CONFIG.listen(GELF_PORT, this::updateGelf);
  }

  @Override
  public void configure(LoggerContext loggerContext) {
    super.configure(loggerContext);
    updateGelf(null);
  }

  private void updateGelf(String changedProp) {
    try {
      LoggerContext loggerContext = (LoggerContext) getContext();
      Logger logbackLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

      GelfAppender<ILoggingEvent> gelfAppender = (GelfAppender<ILoggingEvent>) logbackLogger.getAppender(APPENDER_GELF);

      if (CONFIG.getBoolean(GELF_ENABLED, false)) {
        ConfigContext context = ConfigFactory.getContext();
        String env = context == null ? "n.a." : context.getEnvironment();
        String stack = context == null ? "n.a." : context.getStack();
        String datacenter = context == null ? "n.a." : context.getDatacenter();
        String applicationId = context == null ? "n.a." : context.getApplicationId();
        logbackLogger.detachAppender(gelfAppender);
        gelfAppender = new GelfAppender<>();
        gelfAppender.setHost(CONFIG.getMandatoryString(GELF_HOST));
        gelfAppender.setPort(CONFIG.getMandatoryInt(GELF_PORT));
        gelfAppender.setCompressedChunking(true);
        gelfAppender.setDefaultFields("{\"environment\": \"" + env + "\", \"cluster\": \"" + stack
            + "\", \"facility\": \"" + datacenter + "\", \"application\": \"" + applicationId + "\"}");
        gelfAppender.setAdditionalFields(
            "{\"level\": \"level\", \"logger\": \"loggerName\", \"thread_name\": \"threadName\", \"exception\": \"exception\", \"time_stamp\": \"timestampMs\"}");
        gelfAppender.setName(APPENDER_GELF);
        gelfAppender.setContext(loggerContext);
        gelfAppender.start();
        logbackLogger.addAppender(gelfAppender);
      } else {
        logbackLogger.detachAppender(gelfAppender);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
