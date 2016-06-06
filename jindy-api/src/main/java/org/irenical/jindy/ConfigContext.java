package org.irenical.jindy;

public interface ConfigContext {

  public String getApplicationId();

  public String getDatacenter();

  public String getEnvironment();

  public String getRegion();

  public String getServerId();

  public String getStack();

}