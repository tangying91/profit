package org.profit.app;

import org.profit.app.action.STOCK_POOL_GENERATE;
import org.profit.app.action.STOCK_POOL_LOAD;
import org.profit.server.handler.communication.exception.InvalidActionException;
import org.profit.server.handler.action.Action;

import java.util.HashMap;
import java.util.Map;

public enum Actions {

    INSTANCE;

    private final Map<String, Action> ACTION_MAP;

    Actions() {
        ACTION_MAP = new HashMap<String, Action>();

        ACTION_MAP.put("STOCK_POOL_LOAD", new STOCK_POOL_LOAD());
        ACTION_MAP.put("STOCK_POOL_GENERATE", new STOCK_POOL_GENERATE());
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
