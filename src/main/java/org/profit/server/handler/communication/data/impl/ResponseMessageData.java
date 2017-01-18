package org.profit.server.handler.communication.data.impl;

import org.profit.server.handler.communication.data.AbstractResponseData;

/**
 * Created by TangYing on 14-4-25.
 */
public class ResponseMessageData extends AbstractResponseData {

    private String data = "";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
