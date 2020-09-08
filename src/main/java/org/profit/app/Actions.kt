package org.profit.app

import org.profit.app.action.STOCK_POOL_LOAD
import org.profit.server.handler.action.Action
import org.profit.server.handler.communication.exception.InvalidActionException
import java.util.*

enum class Actions {

    INSTANCE;

    private val ACTION_MAP: MutableMap<String, Action?>
    @Throws(InvalidActionException::class)
    fun interpretCommand(actionName: String?): Action? {
        if (actionName == null || actionName == "") {
            return null
        }
        return if (ACTION_MAP.containsKey(actionName.toUpperCase())) {
            ACTION_MAP[actionName.toUpperCase()]
        } else {
            throw InvalidActionException("Received invalid action id: $actionName")
        }
    }

    init {
        ACTION_MAP = HashMap()
        ACTION_MAP["STOCK_POOL_LOAD"] = STOCK_POOL_LOAD()
    }
}