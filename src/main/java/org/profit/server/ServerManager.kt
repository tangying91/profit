package org.profit.server

/**
 * @author Kyia
 */
interface ServerManager {
    @Throws(Exception::class)
    fun startServers(httpPort: Int)

    @Throws(Exception::class)
    fun startServers()

    @Throws(Exception::class)
    fun stopServers()
}