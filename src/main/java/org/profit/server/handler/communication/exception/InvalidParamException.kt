package org.profit.server.handler.communication.exception

/**
 * Created by Administrator on 14-4-22.
 */
class InvalidParamException : Exception {
    constructor() {}
    constructor(paramString: String?) : super(paramString) {}

    override val message: String?
        get() = super.message

    companion object {
        private const val serialVersionUID = 1L
    }
}