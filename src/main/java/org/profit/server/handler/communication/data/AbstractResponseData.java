package org.profit.server.handler.communication.data;

/**
 * Created by TangYing on 14-4-25.
 */
public abstract class AbstractResponseData {

    protected int error = 101;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
