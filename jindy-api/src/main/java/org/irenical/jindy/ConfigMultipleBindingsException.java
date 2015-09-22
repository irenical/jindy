package org.irenical.jindy;

public class ConfigMultipleBindingsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigMultipleBindingsException() {
        super();
    }

    public ConfigMultipleBindingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConfigMultipleBindingsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigMultipleBindingsException(String message) {
        super(message);
    }

    public ConfigMultipleBindingsException(Throwable cause) {
        super(cause);
    }

}
