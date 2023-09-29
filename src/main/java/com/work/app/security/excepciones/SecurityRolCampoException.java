/**
 *
 */
package com.work.app.security.excepciones;

/**
 * @author enunezt
 */
public class SecurityRolCampoException extends Exception {

    public String name_object = "global";

    /**
     *
     */
    public SecurityRolCampoException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public SecurityRolCampoException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public SecurityRolCampoException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public SecurityRolCampoException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SecurityRolCampoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }
}
