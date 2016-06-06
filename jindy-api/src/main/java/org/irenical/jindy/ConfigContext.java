package org.irenical.jindy;

public class ConfigContext {

  private String applicationId;

  private String serverId;

  private String environment;

  private String stack;

  private String datacenter;

  private String region;

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public void setDatacenter(String datacenter) {
    this.datacenter = datacenter;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public void setStack(String stack) {
    this.stack = stack;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public String getDatacenter() {
    return datacenter;
  }

  public String getEnvironment() {
    return environment;
  }

  public String getRegion() {
    return region;
  }

  public String getServerId() {
    return serverId;
  }

  public String getStack() {
    return stack;
  }

}