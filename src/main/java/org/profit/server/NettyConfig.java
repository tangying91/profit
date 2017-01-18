package org.profit.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author Kyia
 */
public class NettyConfig {

	private Map<ChannelOption<?>, Object> channelOptions;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;
	private int bossThreadCount;
	private int workerThreadCount;
	private InetSocketAddress socketAddress;
	private int portNumber = 18090;
	protected ChannelInitializer<? extends Channel> channelInitializer;

	public Map<ChannelOption<?>, Object> getChannelOptions() {
		return channelOptions;
	}

	public void setChannelOptions(Map<ChannelOption<?>, Object> channelOptions) {
		this.channelOptions = channelOptions;
	}

	public synchronized NioEventLoopGroup getBossGroup() {
		if (bossGroup == null) {
			if (bossThreadCount <= 0) {
				bossGroup = new NioEventLoopGroup();
			} else {
				bossGroup = new NioEventLoopGroup(bossThreadCount);
			}
		}
		return bossGroup;
	}

	public void setBossGroup(NioEventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}

	public synchronized NioEventLoopGroup getWorkerGroup() {
		if (workerGroup == null) {
			if (workerThreadCount <= 0) {
				workerGroup = new NioEventLoopGroup();
			} else {
				workerGroup = new NioEventLoopGroup(workerThreadCount);
			}
		}
		return workerGroup;
	}

	public void setWorkerGroup(NioEventLoopGroup workerGroup) {
		this.workerGroup = workerGroup;
	}

	public int getBossThreadCount() {
		return bossThreadCount;
	}

	public void setBossThreadCount(int bossThreadCount) {
		this.bossThreadCount = bossThreadCount;
	}

	public int getWorkerThreadCount() {
		return workerThreadCount;
	}

	public void setWorkerThreadCount(int workerThreadCount) {
		this.workerThreadCount = workerThreadCount;
	}

	public synchronized InetSocketAddress getSocketAddress() {
		if (socketAddress == null) {
			socketAddress = new InetSocketAddress(portNumber);
		}
		return socketAddress;
	}

	public void setSocketAddress(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
}
