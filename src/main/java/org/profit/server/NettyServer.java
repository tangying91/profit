package org.profit.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author Kyia
 */
public interface NettyServer extends Server {

	public ChannelInitializer<? extends Channel> getChannelInitializer();

	public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer);

	public NettyConfig getNettyConfig();
}
