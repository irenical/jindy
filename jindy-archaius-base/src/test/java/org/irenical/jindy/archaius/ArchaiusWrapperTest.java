package org.irenical.jindy.archaius;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArchaiusWrapperTest {

    private static final String A_PROPERTY = "a.property";

    @Mock
    Configuration configuration;

    @Test
    public void testRemovingAPreviouslyAddedLambdaListener() throws Exception {
        ArchaiusWrapper target = new ArchaiusWrapper(configuration);

        ATestClass testClass = new ATestClass(target);

        testClass.start();
        testClass.stop();

        target.fire(A_PROPERTY);
    }


    private class ATestClass {

        private final ArchaiusWrapper config;

        ATestClass(ArchaiusWrapper wrapper) {
            config = wrapper;
        }

        void start() {
            config.listen(A_PROPERTY, this::aListener);
        }

        void stop() {
            config.unListen(this::aListener);
        }

        private void aListener() {
            Assert.fail("This should never be called");
        }
    }
}
