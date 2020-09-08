package org.profit.server.impl

import org.profit.config.AppContext
import org.profit.server.ServerManager
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Kyia
 */
class ServerManagerImpl : ServerManager {
    private val servers: MutableSet<AbstractNettyServer?>
    @Throws(Exception::class)
    override fun startServers(httpPort: Int) {
        if (httpPort > 0) {
            val tcpServer = AppContext.Companion.getBean(AppContext.Companion.HTTP_SERVER) as AbstractNettyServer
            tcpServer.startServer(httpPort)
            servers.add(tcpServer)
        }
    }

    @Throws(Exception::class)
    override fun startServers() {
        val tcpServer = AppContext.Companion.getBean(AppContext.Companion.HTTP_SERVER) as AbstractNettyServer
        tcpServer.startServer()
        servers.add(tcpServer)
    }

    @Throws(Exception::class)
    override fun stopServers() {
        for (nettyServer in servers) {
            try {
                nettyServer!!.stopServer()
            } catch (e: Exception) {
                LOG.error("Unable to stop server {} due to error {}", nettyServer, e)
                throw e
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ServerManagerImpl::class.java)
    }

    init {
        servers = HashSet()
    }
}