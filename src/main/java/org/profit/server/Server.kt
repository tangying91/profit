package org.profit.server

import java.net.InetSocketAddress

/**
 * @author Kyia
 */
interface Server {
    @Throws(Exception::class)
    fun startServer()

    @Throws(Exception::class)
    fun startServer(port: Int)

    @Throws(Exception::class)
    fun startServer(socketAddress: InetSocketAddress?)

    @Throws(Exception::class)
    fun stopServer()

    val socketAddress: InetSocketAddress?
}