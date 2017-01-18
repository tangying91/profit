package org.profit.server.handler.communication.exception;

/**
 * Created by Administrator on 14-4-22.
 */
public class InvalidParamException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidParamException() {

    }

    public InvalidParamException(String paramString) {
        super(paramString);
    }

    @Override
    public String getMessage() {
        String str = super.getMessage();
        if (str != null) {
            return str;
        }
        return null;
    }
}
