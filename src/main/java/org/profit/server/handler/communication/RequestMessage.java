package org.profit.server.handler.communication;

import org.profit.server.handler.communication.exception.InvalidParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by TangYing on 14-4-22.
 */
public class RequestMessage {

    private static final Logger LOG = LoggerFactory.getLogger(RequestMessage.class);

    private Map<String, String> parameters = new HashMap<String, String>();

    public RequestMessage(Map<String, List<String>> parameters) {
        for (String key : parameters.keySet()) {
            this.parameters.put(key, parameters.get(key).get(0));
        }
    }

    public String getStringParameter(String paramString) {
        return this.parameters.get(paramString);
    }

    public Integer getIntegerParameter(String paramString) {
        String value = this.parameters.get(paramString);
        int param = 0;
        try {
            param = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOG.error("Number format exception. {}, {}", value, e.getMessage());
        }
        return param;
    }

    public Double getDoubleParameter(String paramString) {
        String value = this.parameters.get(paramString);
        double param = 0.0;
        try {
            param = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            LOG.error("Number format exception. {}, {}", value, e.getMessage());
        }
        return param;
    }


    /**
     * Require parameters
     *
     * @param paramArgs
     * @throws InvalidParamException
     */
    public void requireParameters(String... paramArgs) throws InvalidParamException {
        Set<String> localSet = this.parameters.keySet();
        List<String> absentList = new ArrayList<String>();
        for (String arg : paramArgs) {
            if (!localSet.contains(arg)) {
                absentList.add(arg);
            }
        }

        if (!absentList.isEmpty()) {
            InvalidParamException exception = new InvalidParamException("Parameter absent " + absentList.toString());
            throw exception;
        }
    }
}
