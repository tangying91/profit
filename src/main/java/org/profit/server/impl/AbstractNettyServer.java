package org.profit.server.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import org.profit.server.NettyConfig;
import org.profit.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Kyia
 */
public abstract class AbstractNettyServer implements NettyServer {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyServer.class);

	protected final NettyConfig nettyConfig;
	protected ChannelInitializer<? extends Channel> channelInitializer;

	public AbstractNettyServer(NettyConfig nettyConfig, ChannelInitializer<? extends Channel> channelInitializer) {
		this.nettyConfig = nettyConfig;
		this.channelInitializer = channelInitializer;
	}

	@Override
	public void startServer(int port) throws Exception {
		nettyConfig.setPortNumber(port);
		nettyConfig.setSocketAddress(new InetSocketAddress(port));
		startServer();
	}

	@Override
	public void startServer(InetSocketAddress socketAddress) throws Exception {
		nettyConfig.setSocketAddress(socketAddress);
		startServer();
	}

	@Override
	public void stopServer() throws Exception {
		LOG.debug("In stopServer method of class: {}", this.getClass().getName());

		// TODO move this part to spring.
		if (nettyConfig.getBossGroup() != null) {
			nettyConfig.getBossGroup().shutdownGracefully();
		}
		if (nettyConfig.getWorkerGroup() != null) {
			nettyConfig.getWorkerGroup().shutdownGracefully();
		}
	}

	@Override
	public ChannelInitializer<? extends Channel> getChannelInitializer() {
		return channelInitializer;
	}

	@Override
	public NettyConfig getNettyConfig() {
		return nettyConfig;
	}

	protected EventLoopGroup getBossGroup() {
		return nettyConfig.getBossGroup();
	}

	protected EventLoopGroup getWorkerGroup() {
		return nettyConfig.getWorkerGroup();
	}

	@Override
	public InetSocketAddress getSocketAddress() {
		return nettyConfig.getSocketAddress();
	}

	@Override
	public String toString() {
		return "NettyServer [socketAddress=" + nettyConfig.getSocketAddress()
				+ ", portNumber=" + nettyConfig.getPortNumber() + "]";
	}
}
