package org.profit.server.impl

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.profit.server.initializer.HttpServerInitializer
import org.slf4j.LoggerFactory

import java.net.InetSocketAddress

/**
 * NettyHttpServer
 *
 * @author TangYing
 */
class NettyHttpServer(
        /**
         * 端口和监听地址
         */
        private val portNumber: Int) {

    /**
     * 多线程
     */
    var bossGroup: NioEventLoopGroup? = null
    var workerGroup: NioEventLoopGroup? = null

    private var b: ServerBootstrap? = null

    @Throws(Exception::class)
    fun startServer() {
        try {
            b = ServerBootstrap()
            b!!.option(ChannelOption.SO_BACKLOG, 1024)
            b!!.option(ChannelOption.SO_KEEPALIVE, true)

            b!!.group(bossGroup!!, workerGroup!!)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(HttpServerInitializer())

            b!!.bind(InetSocketAddress(portNumber)).sync().channel()

            LOG.info("Http Server started at {}", portNumber)
        } catch (e: Exception) {
            bossGroup!!.shutdownGracefully()
            workerGroup!!.shutdownGracefully()
            LOG.error("Http Server start error {}, going to shut down", e)

            throw e
        }

    }

    fun stopServer() {
        if (b == null) {
            LOG.error("Http Server not existed.")
        }

        bossGroup!!.shutdownGracefully()
        workerGroup!!.shutdownGracefully()
        LOG.info("Http Server stopped at {}", portNumber)
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(NettyHttpServer::class.java)
    }
}
