package org.profit.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.profit.server.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Kyia
 */
public class NettyHttpServer extends AbstractNettyServer {

	private static final Logger LOG = LoggerFactory.getLogger(NettyHttpServer.class);

	private ServerBootstrap serverBootstrap;

	public NettyHttpServer(NettyConfig nettyConfig, ChannelInitializer<? extends Channel> channelInitializer) {
		super(nettyConfig, channelInitializer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startServer() throws Exception {
		try {
			serverBootstrap = new ServerBootstrap();
			Map<ChannelOption<?>, Object> channelOptions = nettyConfig.getChannelOptions();
			if (channelOptions != null) {
				Set<ChannelOption<?>> keySet = channelOptions.keySet();
				for (ChannelOption option : keySet) {
					serverBootstrap.option(option, channelOptions.get(option));
				}
			}
			serverBootstrap.group(getBossGroup(), getWorkerGroup())
					.channel(NioServerSocketChannel.class)
					.childHandler(getChannelInitializer());

			Channel serverChannel = serverBootstrap.bind(nettyConfig.getSocketAddress()).sync().channel();
			LOG.debug("Sever bind channel {}", serverChannel);

			LOG.info("Http Server started at {}", nettyConfig.getPortNumber());
		} catch (Exception e) {
			LOG.error("Http Server start error {}, going to shut down", e);
			super.stopServer();
			throw e;
		}
	}

	@Override
	public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer) {
		this.channelInitializer = initializer;
		serverBootstrap.childHandler(initializer);
	}

	@Override
	public String toString() {
		return "NettyHttpServer [socketAddress=" + nettyConfig.getSocketAddress()
				+ ", portNumber=" + nettyConfig.getPortNumber() + "]";
	}
}
