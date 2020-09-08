package org.profit.server.impl

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import org.profit.server.NettyConfig
import org.profit.server.NettyServer
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

/**
 * @author Kyia
 */
abstract class AbstractNettyServer(override val nettyConfig: NettyConfig, override var channelInitializer: ChannelInitializer<out Channel?>?) : NettyServer {
    @Throws(Exception::class)
    override fun startServer(port: Int) {
        nettyConfig.portNumber = port
        nettyConfig.socketAddress = InetSocketAddress(port)
        startServer()
    }

    @Throws(Exception::class)
    override fun startServer(socketAddress: InetSocketAddress?) {
        nettyConfig.socketAddress = socketAddress
        startServer()
    }

    @Throws(Exception::class)
    override fun stopServer() {
        LOG.debug("In stopServer method of class: {}", this.javaClass.name)
        // TODO move this part to spring.
        nettyConfig.bossGroup?.shutdownGracefully()
        nettyConfig.workerGroup?.shutdownGracefully()
    }

    protected val bossGroup: EventLoopGroup?
        protected get() = nettyConfig.bossGroup

    protected val workerGroup: EventLoopGroup?
        protected get() = nettyConfig.workerGroup

    override val socketAddress: InetSocketAddress?
        get() = nettyConfig.socketAddress

    override fun toString(): String {
        return ("NettyServer [socketAddress=" + nettyConfig.socketAddress
                + ", portNumber=" + nettyConfig.portNumber + "]")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AbstractNettyServer::class.java)
    }

}