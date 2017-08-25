package org.irenical.slf4j;

import org.irenical.lifecycle.LifeCycle;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

public class GelfLoggerLifeCycle implements LifeCycle {

  private boolean started = false;

  @Override
  public void start() {
    if (!started) {
      GelfLoggerConfigurator configurator = new GelfLoggerConfigurator();
      LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
      configurator.setContext(context);
      context.reset();
      configurator.configure(context);
      started = true;
    }
  }

  @Override
  public void stop() {
    ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
    started = false;
  }

  @Override
  public boolean isRunning() {
    return started;
  }

}