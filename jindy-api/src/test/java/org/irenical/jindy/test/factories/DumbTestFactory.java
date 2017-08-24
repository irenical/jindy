package org.irenical.jindy.test.factories;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigNotFoundException;
import org.irenical.jindy.IConfigFactory;
import org.irenical.jindy.PropertyChangedCallback;

public class DumbTestFactory implements IConfigFactory {

  @Override
  public Config createConfig(String name) {
    return new Config() {

      @Override
      public void unListen(String listenerId) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String[] getStringArray(String key) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getString(String key, String defaultValue) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getString(String key) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getMandatoryString(String key) throws ConfigNotFoundException {
        throw new UnsupportedOperationException();
      }

      @Override
      public int getMandatoryInt(String key) throws ConfigNotFoundException {
        throw new UnsupportedOperationException();
      }

      @Override
      public float getMandatoryFloat(String key) throws ConfigNotFoundException {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException {
        throw new UnsupportedOperationException();
      }

      @Override
      public long getMandatoryLong(String key) throws ConfigNotFoundException {
        throw new UnsupportedOperationException();
      }

      @Override
      public int getInt(String key, int defaultValue) {
        throw new UnsupportedOperationException();
      }

      @Override
      public float getFloat(String key, float defaultValue) {
        throw new UnsupportedOperationException();
      }

      @Override
      public long getLong(String key, long defaultValue) {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean getBoolean(String key, boolean defaultValue) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void setProperty(String key, Object value) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Iterable<String> getKeys(String keyPrefix) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String listen(String key, Match keyMatchingRule, PropertyChangedCallback callback) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String listen(String key, PropertyChangedCallback callback) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Config filterPrefix(String prefix) {
        throw new UnsupportedOperationException();
      }
      
      @Override
      public void clearProperty(String key) {
        throw new UnsupportedOperationException();
      }
      
      @Override
      public void clear() {
        throw new UnsupportedOperationException();
      }
      
    };
  }

}
