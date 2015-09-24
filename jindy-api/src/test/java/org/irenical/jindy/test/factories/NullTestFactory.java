package org.irenical.jindy.test.factories;

import org.irenical.jindy.Config;
import org.irenical.jindy.IConfigFactory;

public class NullTestFactory implements IConfigFactory {

    @Override
    public Config createConfig(String name) {
        return null;
    }

}
