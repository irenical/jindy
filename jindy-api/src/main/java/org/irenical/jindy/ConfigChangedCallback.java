package org.irenical.jindy;

@FunctionalInterface
public interface ConfigChangedCallback {

    void propertyChanged(String property, Object value);

}
