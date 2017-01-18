package org.profit.server.impl;

import org.profit.config.AppContext;
import org.profit.server.ServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kyia
 */
public class ServerManagerImpl implements ServerManager {

	private static final Logger LOG = LoggerFactory.getLogger(ServerManagerImpl.class);

	private Set<AbstractNettyServer> servers;

	public ServerManagerImpl() {
		servers = new HashSet<AbstractNettyServer>();
	}

	@Override
	public void startServers(int httpPort) throws Exception {
		if (httpPort > 0) {
			AbstractNettyServer tcpServer = (AbstractNettyServer) AppContext.getBean(AppContext.HTTP_SERVER);
			tcpServer.startServer(httpPort);
			servers.add(tcpServer);
		}
	}

	@Override
	public void startServers() throws Exception {
		AbstractNettyServer tcpServer = (AbstractNettyServer) AppContext.getBean(AppContext.HTTP_SERVER);
		tcpServer.startServer();
		servers.add(tcpServer);
	}

	@Override
	public void stopServers() throws Exception {
		for (AbstractNettyServer nettyServer : servers) {
			try {
				nettyServer.stopServer();
			} catch (Exception e) {
				LOG.error("Unable to stop server {} due to error {}", nettyServer, e);
				throw e;
			}
		}
	}
}
