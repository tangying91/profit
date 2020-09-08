package org.profit.server.handler.communication

import org.profit.server.handler.communication.exception.InvalidParamException
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by TangYing on 14-4-22.
 */
class RequestMessage(parameters: Map<String, List<String?>?>) {
    private val parameters: MutableMap<String, String> = HashMap()
    fun getStringParameter(paramString: String?): String? {
        return parameters[paramString]
    }

    fun getIntegerParameter(paramString: String?): Int {
        val value = parameters[paramString]
        var param = 0
        try {
            param = value!!.toInt()
        } catch (e: NumberFormatException) {
            LOG.error("Number format exception. {}, {}", value, e.message)
        }
        return param
    }

    fun getDoubleParameter(paramString: String?): Double {
        val value = parameters[paramString]
        var param = 0.0
        try {
            param = value!!.toDouble()
        } catch (e: NumberFormatException) {
            LOG.error("Number format exception. {}, {}", value, e.message)
        }
        return param
    }

    /**
     * Require parameters
     *
     * @param paramArgs
     * @throws InvalidParamException
     */
    @Throws(InvalidParamException::class)
    fun requireParameters(vararg paramArgs: String) {
        val localSet: Set<String> = parameters.keys
        val absentList: MutableList<String> = ArrayList()
        for (arg in paramArgs) {
            if (!localSet.contains(arg)) {
                absentList.add(arg)
            }
        }
        if (!absentList.isEmpty()) {
            val exception = InvalidParamException("Parameter absent $absentList")
            throw exception
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RequestMessage::class.java)
    }

    init {
        for (key in parameters.keys) {
            this.parameters[key] = parameters[key]!![0]!!
        }
    }
}