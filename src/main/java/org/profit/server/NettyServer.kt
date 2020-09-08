package org.profit.server

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer

/**
 * @author Kyia
 */
interface NettyServer : Server {
    var channelInitializer: ChannelInitializer<out Channel?>?
    val nettyConfig: NettyConfig
}