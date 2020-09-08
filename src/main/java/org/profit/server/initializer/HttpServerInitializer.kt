package org.profit.server.initializer

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.stream.ChunkedWriteHandler
import org.profit.server.handler.HttpServerHandler

class HttpServerInitializer : ChannelInitializer<SocketChannel>() {
    var httpServerHandler: HttpServerHandler? = null
    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) { // Create a default pipeline implementation.
        val p = ch.pipeline()
        p.addLast("decoder", HttpRequestDecoder())
        p.addLast("aggregator", HttpObjectAggregator(65536))
        p.addLast("encoder", HttpResponseEncoder())
        p.addLast("chunkedWriter", ChunkedWriteHandler())
        p.addLast("handler", httpServerHandler)
    }
}