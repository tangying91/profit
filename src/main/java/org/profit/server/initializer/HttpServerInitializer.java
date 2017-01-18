package org.profit.server.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.profit.server.handler.HttpServerHandler;

/**
 * @author Kyia
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	private HttpServerHandler httpServerHandler;

	public HttpServerInitializer() {

	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline p = ch.pipeline();

		p.addLast("decoder", new HttpRequestDecoder());
		p.addLast("aggregator", new HttpObjectAggregator(65536));
		p.addLast("encoder", new HttpResponseEncoder());
		p.addLast("chunkedWriter", new ChunkedWriteHandler());

		p.addLast("handler", httpServerHandler);
	}

	public HttpServerHandler getHttpServerHandler() {
		return httpServerHandler;
	}

	public void setHttpServerHandler(HttpServerHandler httpServerHandler) {
		this.httpServerHandler = httpServerHandler;
	}
}
