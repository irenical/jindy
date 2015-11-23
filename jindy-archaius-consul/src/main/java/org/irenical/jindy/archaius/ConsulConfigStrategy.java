package org.irenical.jindy.archaius;

import org.irenical.jindy.Config;

public interface ConsulConfigStrategy {
  
  public boolean bypassConsul(Config config);
  
  public String getBasePath(Config config);

}
