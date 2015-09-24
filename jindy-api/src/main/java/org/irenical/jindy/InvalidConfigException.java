package org.irenical.jindy;

public class InvalidConfigException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidConfigException() {
        super();
    }

    public InvalidConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(Throwable cause) {
        super(cause);
    }

}
