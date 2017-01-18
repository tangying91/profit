package org.profit.server.handler.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.profit.server.handler.communication.data.impl.ResponseMessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by TangYing on 14-4-25.
 */
public class ResponseMessage {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseMessage.class);

    private ResponseMessageData INSTANCE;

    public ResponseMessage(String response) {
        try {
            INSTANCE = new Gson().fromJson(response, new TypeToken<ResponseMessageData>(){}.getType());
        } catch (Exception e) {
            INSTANCE = new ResponseMessageData();
        }
    }

    public String getStringValue() {
        return INSTANCE.getData();
    }

    public int getIntegerValue() {
        int value = 0;
        try {
            value = Integer.parseInt(INSTANCE.getData());
        } catch (NumberFormatException e) {
            LOG.error("Number format exception. {}, {}", INSTANCE.getData(), e.getMessage());
        } catch (NullPointerException e) {
            LOG.error("Response format null exception. {}", e.getMessage());
        }
        return value;
    }

    public Double getDoubleValue() {
        double value = 0.0;
        try {
            value = Double.parseDouble(INSTANCE.getData());
        } catch (NumberFormatException e) {
            LOG.error("Number format exception. {}, {}", INSTANCE.getData(), e.getMessage());
        } catch (NullPointerException e) {
            LOG.error("Response format null exception. {}", e.getMessage());
        }
        return value;
    }

    public Long getLongValue() {
        long value = 0L;
        try {
            value = Long.parseLong(INSTANCE.getData());
        } catch (NumberFormatException e) {
            LOG.error("Number format exception. {}, {}", INSTANCE.getData(), e.getMessage());
        } catch (NullPointerException e) {
            LOG.error("Response format null exception. {}", e.getMessage());
        }
        return value;
    }
}
