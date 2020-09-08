package org.profit.server.handler.communication.exception

/**
 * 框架运行时异常
 *
 * @author Miao
 * @version 0.21
 * @since 0.1
 */
class BacException : RuntimeException {
    /**
     * 构建新的运行时异常
     *
     * @since 0.1
     */
    constructor() : super() {}

    /**
     * 用指定的详细消息构建新的运行时异常
     *
     * @param msg 详细消息
     * @since 0.1
     */
    constructor(msg: String?) : super(msg) {}

    /**
     * 用指定的详细消息和原因构造一个新的运行时异常
     *
     * @param msg   详细消息
     * @param cause 原因
     * @since 0.1
     */
    constructor(msg: String?, cause: Throwable?) : super(msg, cause) {}

    /**
     * 用指定的原因和详细消息构造一个新的运行时异常
     *
     * @param cause 原因
     * @since 0.1
     */
    constructor(cause: Throwable?) : super(cause) {}
}