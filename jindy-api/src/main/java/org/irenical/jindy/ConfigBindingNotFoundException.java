package org.irenical.jindy;

public class ConfigBindingNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigBindingNotFoundException() {
        super();
    }

    public ConfigBindingNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConfigBindingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigBindingNotFoundException(String message) {
        super(message);
    }

    public ConfigBindingNotFoundException(Throwable cause) {
        super(cause);
    }

}
