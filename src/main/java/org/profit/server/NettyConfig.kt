package org.profit.server

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import java.net.InetSocketAddress

/**
 * @author Kyia
 */
class NettyConfig {
    var channelOptions: Map<ChannelOption<*>, Any>? = null
    @get:Synchronized
    var bossGroup: NioEventLoopGroup? = null
        get() {
            if (field == null) {
                field = if (bossThreadCount <= 0) {
                    NioEventLoopGroup()
                } else {
                    NioEventLoopGroup(bossThreadCount)
                }
            }
            return field
        }
    @get:Synchronized
    var workerGroup: NioEventLoopGroup? = null
        get() {
            if (field == null) {
                field = if (workerThreadCount <= 0) {
                    NioEventLoopGroup()
                } else {
                    NioEventLoopGroup(workerThreadCount)
                }
            }
            return field
        }
    var bossThreadCount = 0
    var workerThreadCount = 0
    @get:Synchronized
    var socketAddress: InetSocketAddress? = null
        get() {
            if (field == null) {
                field = InetSocketAddress(portNumber)
            }
            return field
        }
    var portNumber = 18090
    protected var channelInitializer: ChannelInitializer<out Channel>? = null

}