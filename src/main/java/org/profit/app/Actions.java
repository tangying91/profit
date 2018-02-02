package org.profit.app;

import org.profit.app.action.STOCK_POOL_LOAD;
import org.profit.server.handler.action.Action;
import org.profit.server.handler.communication.exception.InvalidActionException;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求池
 *
 * @author TangYing
 */
public enum Actions {

    INSTANCE;

    private final Map<String, Action> ACTION_MAP;

    Actions() {
        ACTION_MAP = new HashMap<String, Action>();

        ACTION_MAP.put("STOCK_POOL_LOAD", new STOCK_POOL_LOAD());
    }

    public Action interpretCommand(String actionName) throws InvalidActionException {
        if (actionName == null || actionName.equals("")) {
            return null;
        }

        if (ACTION_MAP.containsKey(actionName.toUpperCase())) {
            return ACTION_MAP.get(actionName.toUpperCase());
        } else {
            throw new InvalidActionException("Received invalid action id: " + actionName);
        }
    }
}
