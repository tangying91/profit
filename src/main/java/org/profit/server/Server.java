package org.profit.server;

import java.net.InetSocketAddress;

/**
 * @author Kyia
 */
public interface Server {

	void startServer() throws Exception;

	void startServer(int port) throws Exception;

	void startServer(InetSocketAddress socketAddress) throws Exception;

	void stopServer() throws Exception;

	InetSocketAddress getSocketAddress();
}
