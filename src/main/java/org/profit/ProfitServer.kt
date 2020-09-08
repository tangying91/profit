package org.profit

import org.profit.config.AppContext
import org.profit.server.ServerManager
import org.profit.server.impl.NettyHttpServer
import org.slf4j.LoggerFactory

class ProfitServer {

    private var started = false

    fun start() {
        val nettyHttpServer = AppContext.getBean(AppContext.HTTP_SERVER) as NettyHttpServer
        try {
            nettyHttpServer.startServer()

            // Engine start
            Engine.INSTANCE.start()
            started = true
            LOG.info("Servers started.")
        } catch (e: Exception) {
            LOG.error("Unable to start servers cleanly: {}", e)
        }
    }

    fun stop() {
        // Engine stop
        Engine.INSTANCE.stop()
        started = false
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProfitServer::class.java)
    }
}